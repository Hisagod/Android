package com.aib.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.aib.base.fragment.BaseLazyFragment
import com.aib.p2p.R
import com.aib.p2p.databinding.FragmentCenterBinding
import com.aib.sdk.arouter.ArouterManager
import com.aib.sdk.arouter.ArouterPath
import com.aib.sdk.event.EventCode
import com.aib.sdk.event.EventData
import com.aib.sdk.sp.SpKeyConstant
import com.aib.sdk.sp.SpManager
import com.aib.utils.GlideManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPStaticUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
@Route(path = ArouterPath.PATH_MINE_PAGE)
class CenterFragment : BaseLazyFragment<FragmentCenterBinding>() {
    @Inject
    lateinit var arouter: ArouterManager

    @Inject
    lateinit var sp: SpManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_center

    override fun initData() {
        openSettings()
        openAvatar()
        loadPersonalInfo()
        enterUserCenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun loadPersonalInfo() {
        GlideManager.getInstance.loadAvatarNetPath(
            binding.ivAvatar,
            sp.getAvatar() ?: ""
        )
        binding.tvMeName.text = sp.getNickName()
    }

    private fun openAvatar() {
        binding.ivAvatar.setOnClickListener {
            arouter.openNext(ArouterPath.PATH_USER_INFO)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(data: EventData<Any>) {
        when (data.code) {
            EventCode.CODE_UPDATE_USER -> {
                loadPersonalInfo()
            }
        }
        EventBus.getDefault().removeStickyEvent(data)
    }

    /**
     * 进入用户中心
     */
    private fun enterUserCenter() {
        binding.rlMe.setOnClickListener {
            arouter.openNext(ArouterPath.PATH_USER_INFO)
        }
    }

    private fun openSettings() {
        binding.ivSettings.setOnClickListener {
            arouter.openNext(ArouterPath.PATH_SETTINGS)
        }
    }
}
