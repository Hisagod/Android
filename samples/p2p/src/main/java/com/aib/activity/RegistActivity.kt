package com.aib.activity

import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.aib.base.activity.BaseToolbarActivity
import com.aib.widget.showDialog
import com.aib.net.Status
import com.aib.p2p.R
import com.aib.p2p.databinding.ActivityUserRegistBinding
import com.aib.sdk.arouter.ArouterPath
import com.aib.viewmodel.RegisterViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = ArouterPath.PATH_REGISTER)
class RegistActivity : BaseToolbarActivity<ActivityUserRegistBinding>() {
    private val vm by viewModels<RegisterViewModel>()

    override fun setTitle(): String = "注册"

    override fun getLayoutId(): Int {
        return R.layout.activity_user_regist
    }

    override fun initData() {
        register()
    }

    fun register() {
        binding.btnRegister.setOnClickListener {
            val getPhone = binding.etPhone.text.toString().trim()
            val getPwd = binding.etPwd.text.toString().trim()

            if (TextUtils.isEmpty(getPhone)) {
                ToastUtils.showShort("请输入手机号")
                return@setOnClickListener
            }

            if (!RegexUtils.isMobileExact(getPhone)) {
                ToastUtils.showShort("请输入正确手机号")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(getPwd)) {
                ToastUtils.showShort("请输入密码")
                return@setOnClickListener
            }

            showDialog { dialog ->
                vm.userRegister(getPhone, EncryptUtils.encryptMD5ToString(getPwd))
                    .observe(this, Observer {
                        when (it.status) {
                            Status.LOAD -> TODO()
                            Status.SUCCESS -> {
                                dialog.dismiss()
                                finish()
                            }
                            Status.ERROR -> {
                                dialog.dismiss()
                                ToastUtils.showShort(it.message)
                            }
                            Status.EMPTY -> TODO()
                        }
                    })
            }
        }
    }
}