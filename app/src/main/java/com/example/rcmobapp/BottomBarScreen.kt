package com.example.rcmobapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen (
        route = "home",
        title = "Главная",
        icon = Icons.Default.Home
    )

    object Config: BottomBarScreen (
        route = "config",
        title = "Конфигурация",
        icon = Icons.Default.Settings
    )
}