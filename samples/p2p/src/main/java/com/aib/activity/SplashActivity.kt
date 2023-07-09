package com.aib.activity

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aib.p2p.R
import com.aib.sdk.arouter.ArouterManager
import com.aib.sdk.arouter.ArouterPath
import com.aib.viewmodel.SplashViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.FullCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Route(path = ArouterPath.PATH_SPLASH)
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var arouter: ArouterManager

    private val vm by viewModels<SplashViewModel>()

    private val permissions = arrayOf(
        PermissionConstants.STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        setContent {
            showSplash()
        }

        PermissionUtils.permission(*permissions).callback(object : FullCallback {
            override fun onGranted(permissionsGranted: List<String>) {
                object : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        arouter.openNext(ArouterPath.PATH_MAIN)
                        finish()
                    }
                }.start()
            }

            override fun onDenied(
                permissionsDeniedForever: List<String>,
                permissionsDenied: List<String>
            ) {

            }
        }).request()
    }
}

@Composable
fun showSplash() {
    Box {
        Image(
            painter = painterResource(R.drawable.start_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.mipmap.app_icon),
                contentDescription = null,
                modifier = Modifier.padding(top = 100.dp).wrapContentSize()
            )
            Text(
                "硅谷金融",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 20.dp).wrapContentSize()
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "版本号：V${AppUtils.getAppVersionName()}",
                fontSize = 15.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 100.dp).wrapContentSize()
            )
        }
    }
}