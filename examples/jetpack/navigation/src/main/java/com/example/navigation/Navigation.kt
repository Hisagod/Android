package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Jump() {
    val control = rememberNavController()
    NavHost(navController = control, startDestination = "") {
        composable("first") {

        }
        composable("second") {

        }
    }
}
