package com.aib.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Jump() {
    val control = rememberNavController()

    NavHost(navController = control, startDestination = RouterConstant.SPLASH) {
        composable(RouterConstant.SPLASH) {
            Splash()
        }
    }
}