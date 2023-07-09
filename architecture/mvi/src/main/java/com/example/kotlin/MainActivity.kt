package com.example.kotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mvi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            val phone = binding.phone.text.toString()
            if (phone.isEmpty())
                return@setOnClickListener
            val pwd = binding.pwd.text.toString()
            if (pwd.isEmpty())
                return@setOnClickListener
            sendIntent(MainIntent.Login(phone, pwd))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.onEach {
                    when (it) {
                        is MainUiState.Loading -> {
                            Log.e("HLP", "加载中")
                        }

                        is MainUiState.Success -> {
                            Log.e("HLP", "加载成功：${it.data}")
                        }

                        is MainUiState.Error -> {
                            Log.e("HLP", "加载失败")
                        }

                        else -> {

                        }
                    }
                }.collect()
            }
        }
    }

    private fun sendIntent(intent: MainIntent) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.mainIntent.send(intent)
            }
        }
    }
}