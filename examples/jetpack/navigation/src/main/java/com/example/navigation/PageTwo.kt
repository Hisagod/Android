package com.example.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@Composable
fun PageTwo(control: NavHostController) {
    Column {
        Text(text = "第二个页面")
        Button(onClick = {
            control.navigate(RouterConstant.FIRST_PAGE)
        }) {
            Text(text = "返回第一个页面")
        }
    }
}