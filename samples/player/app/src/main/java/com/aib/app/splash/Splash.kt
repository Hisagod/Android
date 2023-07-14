package com.aib.app.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.atguigu.mobileplayer2.R

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
}

private fun sendIntent(intent: SplashIntent) {
    
}