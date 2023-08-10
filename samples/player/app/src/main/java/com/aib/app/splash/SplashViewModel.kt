package com.aib.app.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SplashRepository
) : ViewModel() {
    val splashIntent = Channel<SplashIntent>()

    private val _uiState = MutableSharedFlow<SplashUiState>()
    val uiState: SharedFlow<SplashUiState> = _uiState

    init {
        viewModelScope.launch {
            splashIntent.consumeAsFlow().onEach {
                when (it) {
                    is SplashIntent.EnterMain -> {
                        enterMain()
                    }
                }
            }.catch {

            }.collect()
        }
    }

    private suspend fun enterMain() {
        _uiState.emit(SplashUiState.Success)
    }
}