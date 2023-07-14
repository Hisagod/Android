package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Jump() {
    val control = rememberNavController()
    NavHost(navController = control, startDestination = RouterConstant.FIRST_PAGE) {
        composable(RouterConstant.FIRST_PAGE) {
            PageOne(control)
        }

        composable(RouterConstant.SECOND_PAGE) {
            PageTwo(control)
        }
    }
}
