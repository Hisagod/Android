package com.aib.app.home

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aib.app.RouterConstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val tab = mutableListOf(BottomTab.AudioTab, BottomTab.VideoTab)

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
            startDestination = RouterConstant.TAB_AUDIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(RouterConstant.TAB_AUDIO) {
                Audio(control)
            }

            composable(RouterConstant.TAB_VIDEO) {
                Video(control)
            }
        }
    }
}