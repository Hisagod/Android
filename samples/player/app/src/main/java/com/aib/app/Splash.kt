package com.aib.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.atguigu.mobileplayer2.R

@Composable
fun Splash() {
    Image(
        painterResource(id = R.drawable.login_icon),
        contentDescription = "",
        modifier = Modifier.size(80.dp),
        alignment = Alignment.Center
    )
}