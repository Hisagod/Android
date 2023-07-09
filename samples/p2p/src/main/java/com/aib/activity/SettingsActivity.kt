package com.aib.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aib.p2p.R
import com.aib.sdk.arouter.ArouterManager
import com.aib.sdk.arouter.ArouterPath
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Route(path = ArouterPath.PATH_SETTINGS)
class SettingsActivity : AppCompatActivity() {
    @Inject
    lateinit var arouter: ArouterManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(topBar = {
                TopAppBar(backgroundColor = colorResource(R.color.colorPrimary)) {
                    Image(
                        painter = painterResource(R.drawable.ic_back),
                        null,
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .clickable { finish() }
                            .padding(start = 7.dp, end = 7.dp)
                    )
                }
            }) {
                Column {
                    settingItem(R.drawable.icon_more_contact, "联系客服") {
                        ToastUtils.showShort("联系客服")
                    }

                    settingItem(R.drawable.icon_more_sms, "用户反馈") {
                        ToastUtils.showShort("用户反馈")
                    }

                    settingItem(R.drawable.icon_more_share, "分享好友") {
                        ToastUtils.showShort("分享好友")
                    }

                    settingItem(R.drawable.icon_more_about, "关于我们") {
                        ToastUtils.showShort("关于我们")
                    }
                }
            }
        }
    }
}

@Composable
fun settingItem(tipIcon: Int, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() }
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(tipIcon),
            null
        )
        Text(
            text,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}