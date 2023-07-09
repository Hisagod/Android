package com.aib.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun imageView(modifier: Modifier, @DrawableRes res: Int) {
    Image(
        painter = painterResource(res),
        contentDescription = null,
        modifier = modifier
    )
}