package com.pos.kasir.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MenuManageScreen(nav: NavController) {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Kelola Menu", style = MaterialTheme.typography.headlineSmall)
        Text("Gunakan backend/web untuk tambah menu (admin).")
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { nav.popBackStack() }) { Text("Kembali") }
    }
}
