package com.example.compose

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

/**
 * 用户输入控件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun EditText() {
    var text1 by remember {
        mutableStateOf("")
    }

    TextField(
        value = text1,
        onValueChange = { text1 = it },
        label = {
            Text(
                text = "请输入手机号-TextField"
            )
        })

    var text2 by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text2,
        onValueChange = { text2 = it },
        label = {
            Text(
                text = "请输入手机号-OutlinedTextField"
            )
        })

    var text3 by remember {
        mutableStateOf("")
    }

    TextField(
        value = text3,
        onValueChange = { text3 = it },
        label = {
            Text(
                text = "请输入密码"
            )
        },
        //输入的内容变为*
        visualTransformation = PasswordVisualTransformation(),
        //键盘多出数字一栏
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )

    var text4 by remember {
        mutableStateOf("")
    }

    TextField(
        value = text4,
        onValueChange = { text4 = it.trimStart { it == '0' } },
        label = {
            Text(
                text = "禁止开头输入为0的字符"
            )
        })
}