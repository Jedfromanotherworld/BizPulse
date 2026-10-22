package com.bizpulse.app.util
import android.content.Context
import android.content.Intent
import android.content.ClipData
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.room.withTransaction
import com.bizpulse.app.data.db.*
import com.bizpulse.app.data.db.entity.*
import com.bizpulse.app.data.repository.*
import com.bizpulse.app.ui.session.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.*
import org.json.*
import java.io.File
import java.time.*

class BackupManager @Inject constructor(@ApplicationContext private val context: Context, private val db: BizPulseDatabase, private val users: UserRepository, private val audit: AuditRepository, private val session: SessionManager) {
    suspend fun export(): Uri = withContext(Dispatchers.IO) {
        val actor=users.requireOwner()
        val json=db.withTransaction {
            JSONObject().put("format","BizPulse").put("version",1).put("exportedAt",System.currentTimeMillis())
                .put("users",array(db.users().snapshot().map(::userJson)))
                .put("categories",array(db.categories().snapshot().map(::categoryJson)))
                .put("products",array(db.products().snapshot().map(::productJson)))
                .put("transactions",array(db.transactions().snapshot().map(::transactionJson)))
                .put("lineItems",array(db.lines().snapshot().map(::lineJson)))
                .put("auditLog",array(db.audit().snapshot().map(::auditJson)))
        }
        val dir=File(context.cacheDir,"backups").apply { mkdirs() }
        dir.listFiles()?.filter { System.currentTimeMillis()-it.lastModified()>86400000L }?.forEach { it.delete() }
        val file=File(dir,"BizPulse-${System.currentTimeMillis()}.json")
        file.writeText(json.toString(2))
        audit.log(actor.name,"Backup Exported","All six database tables exported","create")
        FileProvider.getUriForFile(context,"${context.packageName}.fileprovider",file)
    }
    fun share(uri: Uri): Intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type="application/json"; putExtra(Intent.EXTRA_STREAM,uri)
        clipData=ClipData.newRawUri("BizPulse backup",uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    },"Share BizPulse backup")
    suspend fun import(uri: Uri) = withContext(Dispatchers.IO) {
        val actor=users.requireOwner()
        val text=context.contentResolver.openInputStream(uri)?.use { input ->
            val bytes=input.readBytesLimited(10*1024*1024)
            bytes.toString(Charsets.UTF_8)
        } ?: error("Cannot open this file")
        val j=JSONObject(text)
        require(j.getString("format")=="BizPulse" && j.getInt("version")==1) { "Unsupported backup format" }
        val u=rows(j,"users").map { o -> UserEntity(o.getLong("id"),o.getString("name"),o.getString("username"),o.getString("passwordHash"),o.getString("role"),o.getLong("createdAt")) }
        val c=rows(j,"categories").map { o -> CategoryEntity(o.getLong("id"),o.getString("name"),o.getString("description")) }
        val p=rows(j,"products").map { o -> ProductEntity(o.getLong("id"),o.getString("name"),o.getString("barcode"),o.getDouble("price"),o.getDouble("cost"),o.getInt("stock"),o.getInt("minAlert"),o.getLong("categoryId"),o.getLong("createdAt")) }
        val t=rows(j,"transactions").map { o -> TransactionEntity(o.getLong("id"),o.getString("date"),o.getString("time"),o.getDouble("total"),o.getInt("items"),o.getString("payment"),o.getString("cashierName"),o.getLong("cashierId"),o.getDouble("cost"),o.getDouble("profit"),o.getLong("createdAt"),o.getBoolean("manual"),o.getDouble("tendered")) }
        val l=rows(j,"lineItems").map { o -> LineItemEntity(o.getLong("id"),o.getLong("transactionId"),o.getLong("productId"),o.getString("productName"),o.getInt("quantity"),o.getDouble("price"),o.getDouble("cost"),o.getLong("categoryId")) }
        val a=rows(j,"auditLog").map { o -> AuditLogEntity(o.getLong("id"),o.getLong("timestamp"),o.getString("userName"),o.getString("action"),o.getString("details"),o.getString("type")) }
        listOf(u.map { it.id },c.map { it.id },p.map { it.id },t.map { it.id },l.map { it.id },a.map { it.id }).forEach { ids -> require(ids.all { it>0 } && ids.distinct().size==ids.size) { "Duplicate or invalid IDs" } }
        require(u.any { it.role=="owner" } && u.all { it.name.isNotBlank() && Validation.username(it.username) && it.username==it.username.lowercase() && Validation.role(it.role) && it.passwordHash.matches(Regex("[0-9a-f]{64}")) }) { "Invalid user records" }
        require(u.map { it.username }.distinct().size==u.size) { "Duplicate usernames" }
        require(c.all { it.name.isNotBlank() } && c.map { it.name.lowercase() }.distinct().size==c.size) { "Invalid categories" }
        require(p.all { it.name.isNotBlank() && it.barcode.isNotBlank() && Validation.price(it.price) && Validation.amount(it.cost) && Validation.count(it.stock) && Validation.count(it.minAlert) && c.any { cat -> cat.id==it.categoryId } } && p.map { it.barcode }.distinct().size==p.size) { "Invalid products or categories" }
        require(l.all { it.productId>0 && it.productName.isNotBlank() && it.quantity in 1..1_000_000 && Validation.price(it.price) && Validation.amount(it.cost) && c.any { cat -> cat.id==it.categoryId } && t.any { sale -> sale.id==it.transactionId } }) { "Invalid receipt items" }
        t.forEach { sale ->
            LocalDate.parse(sale.date); LocalTime.parse(sale.time)
            require(Validation.price(sale.total) && Validation.amount(sale.cost) && sale.profit.isFinite() && sale.items>0 && Validation.payment(sale.payment) && sale.cashierId>0 && sale.cashierName.isNotBlank() && Validation.amount(sale.tendered)) { "Invalid transactions" }
            require(kotlin.math.abs(sale.profit-rounded(sale.total-sale.cost))<0.011) { "Profit does not match transaction" }
            val lines=l.filter { it.transactionId==sale.id }
            require(sale.manual == lines.isEmpty()) { "Manual/receipt mismatch" }
            if(lines.isNotEmpty()) {
                val subtotal=rounded(lines.sumOf { it.price*it.quantity })
                require(kotlin.math.abs(sale.total-rounded(subtotal+rounded(subtotal*0.12)))<0.011 && sale.items==lines.sumOf { it.quantity } && kotlin.math.abs(sale.cost-rounded(lines.sumOf { it.cost*it.quantity }))<0.011) { "Receipt totals do not match line items" }
            }
        }
        db.withTransaction {
            clear()
            u.forEach { db.users().insert(it) }; c.forEach { db.categories().insert(it) }; p.forEach { db.products().insert(it) }
            t.forEach { db.transactions().insert(it) }; db.lines().insert(l); a.forEach { db.audit().insert(it) }
            audit.log(actor.name,"Backup Imported","Validated backup replaced all database tables","update")
        }
        session.logout()
    }
    suspend fun reset() = withContext(Dispatchers.IO) {
        users.requireOwner()
        db.withTransaction { clear(); DatabaseSeeder.seed(db.openHelper.writableDatabase) }
        session.setTarget(7500.0)
        session.logout()
    }
    private suspend fun clear() {
        db.lines().clear(); db.transactions().clear(); db.products().clear(); db.categories().clear(); db.users().clear(); db.audit().clear()
    }
    private fun java.io.InputStream.readBytesLimited(limit: Int): ByteArray {
        val out=java.io.ByteArrayOutputStream(); val buf=ByteArray(8192)
        while(true) { val n=read(buf); if(n<0) break; require(out.size()+n<=limit) { "Backup exceeds 10 MB limit" }; out.write(buf,0,n) }
        return out.toByteArray()
    }
    private fun rows(j: JSONObject, key: String): List<JSONObject> { val a=j.getJSONArray(key); require(a.length()<=100000) { "Too many records" }; return (0 until a.length()).map { a.getJSONObject(it) } }
    private fun array(rows: List<JSONObject>) = JSONArray().apply { rows.forEach { put(it) } }
    private fun userJson(x:UserEntity) = JSONObject().put("id",x.id).put("name",x.name).put("username",x.username).put("passwordHash",x.passwordHash).put("role",x.role).put("createdAt",x.createdAt)
    private fun categoryJson(x:CategoryEntity) = JSONObject().put("id",x.id).put("name",x.name).put("description",x.description)
    private fun productJson(x:ProductEntity) = JSONObject().put("id",x.id).put("name",x.name).put("barcode",x.barcode).put("price",x.price).put("cost",x.cost).put("stock",x.stock).put("minAlert",x.minAlert).put("categoryId",x.categoryId).put("createdAt",x.createdAt)
    private fun transactionJson(x:TransactionEntity) = JSONObject().put("id",x.id).put("date",x.date).put("time",x.time).put("total",x.total).put("items",x.items).put("payment",x.payment).put("cashierName",x.cashierName).put("cashierId",x.cashierId).put("cost",x.cost).put("profit",x.profit).put("createdAt",x.createdAt).put("manual",x.manual).put("tendered",x.tendered)
    private fun lineJson(x:LineItemEntity) = JSONObject().put("id",x.id).put("transactionId",x.transactionId).put("productId",x.productId).put("productName",x.productName).put("quantity",x.quantity).put("price",x.price).put("cost",x.cost).put("categoryId",x.categoryId)
    private fun auditJson(x:AuditLogEntity) = JSONObject().put("id",x.id).put("timestamp",x.timestamp).put("userName",x.userName).put("action",x.action).put("details",x.details).put("type",x.type)
}
