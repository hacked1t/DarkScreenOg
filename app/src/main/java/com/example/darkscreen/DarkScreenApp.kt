package com.example.darkscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.darkscreen.ui.theme.DarkSystemBars
import com.darkscreen.ui.screens.TimerScreen
import com.example.darkscreen.ui.screens.HomeScreen
import com.example.darkscreen.ui.theme.DarkScreenTheme

@Composable
fun DarkScreenApp() {
    DarkScreenTheme {
        DarkSystemBars()
        val nav = rememberNavController()
        NavHost(navController = nav, startDestination = "home", modifier = Modifier) {
            composable("home") {
                HomeScreen(onStart = { minutes ->
                    nav.navigate("timer/$minutes")
                })
            }
            composable("timer/{minutes}") { backStackEntry ->
                val minutes = backStackEntry.arguments?.getString("minutes")?.toIntorNull() ?: 25
                TimerScreen(
                    minutes = minutes,
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}