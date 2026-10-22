package com.bizpulse.app.data.db
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bizpulse.app.data.db.dao.*
import com.bizpulse.app.data.db.entity.*
@Database(entities = [UserEntity::class, CategoryEntity::class, ProductEntity::class, TransactionEntity::class, LineItemEntity::class, AuditLogEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BizPulseDatabase : RoomDatabase() {
    abstract fun users(): UserDao
    abstract fun categories(): CategoryDao
    abstract fun products(): ProductDao
    abstract fun transactions(): TransactionDao
    abstract fun lines(): LineItemDao
    abstract fun audit(): AuditLogDao
    companion object {
        @Volatile private var instance: BizPulseDatabase? = null
        fun getInstance(context: Context): BizPulseDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, BizPulseDatabase::class.java, "bizpulse.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) { DatabaseSeeder.seed(db) }
                }).build().also { instance = it }
        }
    }
}
