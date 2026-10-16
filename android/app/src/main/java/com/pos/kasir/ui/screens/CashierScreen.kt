package com.pos.kasir.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pos.kasir.viewmodel.CashierViewModel

@Composable
fun CashierScreen(nav: NavController) {
    val vm: CashierViewModel = viewModel()
    val menu by vm.menu.collectAsState()
    val cart by vm.cart.collectAsState()
    val orders by vm.orders.collectAsState()
    val msg by vm.message.collectAsState()

    LaunchedEffect(msg) { if (msg != null) vm.clearMessage() }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Kasir", style = MaterialTheme.typography.headlineSmall)
            Row {
                TextButton(onClick = { nav.navigate("kitchen") }) { Text("Dapur") }
                TextButton(onClick = { nav.navigate("menu") }) { Text("Menu") }
            }
        }
        Text("Menu", style = MaterialTheme.typography.titleMedium)
        LazyColumn(Modifier.height(200.dp)) {
            items(menu.size) { i ->
                val m = menu[i]
                ListItem(
                    headlineContent = { Text(m.name) },
                    supportingContent = { Text("Rp ${m.price} - ${m.category}") },
                    trailingContent = { Button(onClick = { vm.addToCart(m) }) { Text("+") } }
                )
            }
        }
        Divider()
        Text("Keranjang", style = MaterialTheme.typography.titleMedium)
        cart.forEach { line ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${line.menu.name} x${line.qty}")
                Row {
                    TextButton(onClick = { vm.changeQty(line.menu.id, -1) }) { Text("-") }
                    TextButton(onClick = { vm.changeQty(line.menu.id, 1) }) { Text("+") }
                }
            }
        }
        Button(onClick = { vm.checkout() }, enabled = cart.isNotEmpty()) { Text("Bayar") }
        Divider()
        Text("Pesanan", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(orders.size) { i ->
                val o = orders[i]
                ListItem(
                    headlineContent = { Text("${o.id} - ${o.status}") },
                    supportingContent = { Text(o.items.joinToString { "${it.name} x${it.qty}" }) },
                    trailingContent = { if (o.status != "completed") TextButton(onClick = { vm.completeOrder(o.id) }) { Text("Selesai") } }
                )
            }
        }
    }
}
