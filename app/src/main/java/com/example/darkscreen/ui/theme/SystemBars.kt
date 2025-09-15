package com.example.darkscreen.ui.theme

import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color

@Composable
fun DarkSystemBars() {
    val sys = rememberSystemUiController()
    SideEffect {
        sys.setSystemBarsColor(color = Color(0xFF0B0B0D))
        sys.setNavigationBarColor(color = Color(0xFF0B0B0D))

    }
}