package com.example.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

/**
 * 文本控件
 */
@Composable
@Preview
fun TextView() {
    //显示文本
    Text(text = "显示文本")
    //显示资源
    Text(text = stringResource(id = R.string.show_resource))
    //文本颜色
    Text(text = "文本颜色", color = Color.Red)
    //文本大小
    Text(text = "文本大小", fontSize = 30.sp)
    //斜体
    Text(text = "斜体", fontStyle = FontStyle.Italic)
    //粗体
    Text(text = "粗体", fontWeight = FontWeight.Bold)
    //文本对齐
    Text(text = "文本对齐", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    //设置阴影
    Text(
        text = "设置阴影",
        style = TextStyle(shadow = Shadow(color = Color.Red, Offset(5f, 10f), blurRadius = 3f))
    )
    //处理字体
    Text(text = "处理字体", fontFamily = FontFamily.Cursive)
    //多样式
    Text(text = buildAnnotatedString {
        withStyle(style = SpanStyle(Color.Green)) {
            append("多")
        }
        append("样")
        withStyle(style = SpanStyle(color = Color.Red, fontSize = 30.sp)) {
            append("式")
        }
    })
    //行数上限
    Text(text = "行数上限".repeat(100), maxLines = 2)
    //文字溢出
    Text(text = "文字溢出".repeat(100), maxLines = 2, overflow = TextOverflow.Ellipsis)
    //选择文本
    SelectionContainer {
        Text(text = "选择文本")
    }
    //不可选择文本
    SelectionContainer {
        Column {
            Text(text = "不可")
            DisableSelection {
                Text(text = "选择")
            }
            Text(text = "文本")
        }
    }
}

