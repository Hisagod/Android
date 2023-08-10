package com.aib.app.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aib.app.RouterConstant
import com.atguigu.mobileplayer2.R
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun Splash(control: NavHostController) {
//    val vm: SplashViewModel = viewModel()
    val vm = hiltViewModel<SplashViewModel>()

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(id = R.drawable.login_icon),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
    }

    RequestPermission(success = { /*TODO*/ }) {
        LaunchedEffect(key1 = true, block = {
            delay(2000)
            vm.splashIntent.send(SplashIntent.EnterMain)
        })
    }

//    LaunchedEffect(key1 = true, block = {
//        delay(2000)
//        vm.splashIntent.send(SplashIntent.EnterMain)
//    })

    LaunchedEffect(key1 = true, block = {
        vm.uiState.onEach {
            when (it) {
                is SplashUiState.Success -> {
                    control.navigate(RouterConstant.HOME)
                }
            }
        }.collect()
    })
}