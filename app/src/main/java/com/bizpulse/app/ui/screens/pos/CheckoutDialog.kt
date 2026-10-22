package com.bizpulse.app.ui.screens.pos
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bizpulse.app.ui.components.*
import com.bizpulse.app.data.model.CartLogic
import com.bizpulse.app.util.*
@Composable fun CheckoutDialog(vm:PosViewModel) {
    val ui by vm.ui.collectAsStateWithLifecycle(); val cart by vm.cart.collectAsStateWithLifecycle()
    val total=CartLogic.total(cart); val cash=ui.fields["cash"]?.toDoubleOrNull() ?: 0.0; val payment=ui.fields["payment"] ?: "Cash"
    AlertDialog(onDismissRequest={if(!ui.busy) vm.close()},title={Text("Checkout")},text={Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Text("Total due: ${money(total)}",style=MaterialTheme.typography.titleLarge)
        Choice("Payment method",listOf("Cash","Card","GCash").map {it to it},payment,vm) {vm.field("payment",it)}
        if(payment=="Cash") {Field("Cash Tendered",ui.fields["cash"] ?: "",{vm.field("cash",it)},numeric=true); Text(if(cash>=total) "Change: ${money(cash-total)}" else "Amount still due: ${money(total-cash)}")}
        else Text("Confirm the payment was received before completing this sale.")
        Notice(ui,vm::dismissMessage)
    }},confirmButton={TextButton(onClick=vm::checkout,enabled=!ui.busy && (payment!="Cash" || cash.isFinite() && cash>=total)) {Text(if(ui.busy) "Processing…" else "Complete")}},dismissButton={TextButton(onClick=vm::close,enabled=!ui.busy) {Text("Cancel")}})
}
