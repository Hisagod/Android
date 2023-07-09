package com.aib.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import com.aib.base.activity.BaseToolbarActivity
import com.aib.p2p.R
import com.aib.p2p.databinding.ActivityUserInfoBinding
import com.aib.sdk.arouter.ArouterPath
import com.aib.sdk.sp.SpKeyConstant
import com.aib.sdk.sp.SpManager
import com.aib.utils.GifSizeFilter
import com.aib.utils.GlideManager.Companion.getInstance
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.UriUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Route(path = ArouterPath.PATH_USER_INFO)
class UserInfoActivity : BaseToolbarActivity<ActivityUserInfoBinding>() {
    private val TAG = javaClass.simpleName
    private val REQUEST_CODE_CHOOSE = 0

    @Inject
    lateinit var spManager: SpManager

    override fun setTitle(): String {
        return "用户资料"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_info
    }

    override fun initData() {
        loadLocalData()
        userExit()
        openGallery()
    }

    private fun loadLocalData() {
        getInstance.loadAvatarLocalPath(binding.ivAvatar, SPStaticUtils.getString(SpKeyConstant.KEY_STRING_USER_AVATAR))
    }

    /**
     * 打开相册
     */
    private fun openGallery() {
        binding.tvUserChange.setOnClickListener {
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .maxSelectable(1)
                    .gridExpectedSize(300)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(GlideEngine())
                    .showPreview(false)
                    .forResult(REQUEST_CODE_CHOOSE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CHOOSE -> {
                    //相册预览结果
                    val result = Matisse.obtainResult(data)
                    LogUtils.eTag(TAG, UriUtils.uri2File(result[0]).absolutePath)
                }
            }
        }
    }

    private fun userExit() {
        binding.btnExit.setOnClickListener {
            spManager.clearAllData()
            ARouter.getInstance().build(ArouterPath.PATH_MAIN).navigation()
            finish()
        }
    }
}