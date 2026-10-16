package com.pos.kasir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pos.kasir.ui.screens.CashierScreen
import com.pos.kasir.ui.screens.KitchenScreen
import com.pos.kasir.ui.screens.LoginScreen
import com.pos.kasir.ui.screens.MenuManageScreen
import com.pos.kasir.ui.theme.PosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PosTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()
                    NavHost(navController = nav, startDestination = "login") {
                        composable("login") { LoginScreen(nav) }
                        composable("cashier") { CashierScreen(nav) }
                        composable("kitchen") { KitchenScreen(nav) }
                        composable("menu") { MenuManageScreen(nav) }
                    }
                }
            }
        }
    }
}
