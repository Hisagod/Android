package com.example.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@Composable
fun PageOne(control: NavHostController) {
    Column {
        Text(text = "第一个页面", modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            control.navigate(RouterConstant.SECOND_PAGE)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "跳转第二个页面")
        }
    }
}