package com.pos.kasir.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pos.kasir.viewmodel.KitchenViewModel

@Composable
fun KitchenScreen(nav: NavController) {
    val vm: KitchenViewModel = viewModel()
    val orders by vm.orders.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Layar Dapur", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = { nav.popBackStack() }) { Text("Kembali") }
        }
        Button(onClick = { vm.refresh() }) { Text("Refresh") }
        LazyColumn {
            items(orders.size) { i ->
                val o = orders[i]
                Card(Modifier.fillMaxWidth().padding(4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${o.id} - ${o.status}", style = MaterialTheme.typography.titleMedium)
                        Text(o.items.joinToString { "${it.name} x${it.qty}" })
                        Row {
                            if (o.status == "pending") Button(onClick = { vm.setStatus(o.id, "preparing") }) { Text("Mulai Masak") }
                            if (o.status == "preparing") Button(onClick = { vm.setStatus(o.id, "ready") }) { Text("Siap") }
                            if (o.status == "ready") Button(onClick = { vm.setStatus(o.id, "completed") }) { Text("Selesai") }
                        }
                    }
                }
            }
        }
    }
}
