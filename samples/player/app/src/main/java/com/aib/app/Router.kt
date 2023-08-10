package com.aib.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aib.app.home.Home
import com.aib.app.splash.Splash

@Composable
fun Jump() {
    val control = rememberNavController()
    NavHost(
        navController = control,
        startDestination = RouterConstant.SPLASH
    ) {
        composable(RouterConstant.SPLASH) {
            Splash(control)
        }

        composable(RouterConstant.HOME) {
            Home()
        }
    }
}