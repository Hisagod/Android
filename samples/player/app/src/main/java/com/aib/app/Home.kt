package com.aib.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(route: String) {
    val tab = mutableListOf(BottomTab.VideoTab, BottomTab.AudioTab)

    val control = rememberNavController()

    Scaffold(bottomBar = {
        BottomNavigation {
            val navBackStackEntry by control.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            tab.forEach { tab ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = tab.icon),
                            contentDescription = ""
                        )
                    },
                    label = { Text(text = tab.text) },
                    selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                    onClick = {
                        control.navigate(tab.route) {
                            popUpTo(control.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }) { innerPadding ->
        NavHost(
            navController = control,
            startDestination = RouterConstant.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(RouterConstant.TAB_AUDIO) {

            }

            composable(RouterConstant.TAB_AUDIO) {

            }
        }
    }
}