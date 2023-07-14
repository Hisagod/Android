package com.example.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
@Preview
fun ImageView() {
    //加载本地资源
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null
    )

    //加载网络图片
    AsyncImage(
        model = "https://res.naadi.microparty.com/user/1673946183827.jpeg",
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.FillWidth
    )

    //缩放图片
    Image(
        painter = painterResource(id = R.drawable.test),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
    )

    //裁剪
    Image(
        painter = painterResource(id = R.drawable.test),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .size(200.dp),
        contentScale = ContentScale.Crop
    )
}