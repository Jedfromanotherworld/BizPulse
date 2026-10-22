package com.bizpulse.app.ui.screens.inventory
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bizpulse.app.ui.components.*
@Composable fun ProductEditDialog(vm:InventoryViewModel) {
    val ui by vm.ui.collectAsStateWithLifecycle(); val d by vm.data.collectAsStateWithLifecycle()
    FormDialog(if(ui.id==0L) "Add Product" else "Edit Product",ui,vm::close,vm::save) {
        listOf("name" to "Product name","barcode" to "Barcode","price" to "Price (before VAT)","cost" to "Cost","stock" to "Stock","minAlert" to "Minimum alert").forEach { (key,label) -> Field(label,ui.fields[key] ?: "",{vm.field(key,it)},numeric=key in listOf("price","cost","stock","minAlert")) }
        Choice("Category",d.categories.map { it.id.toString() to it.name },ui.fields["categoryId"] ?: "",vm) { vm.field("categoryId",it) }
    }
}
