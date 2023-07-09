package com.example.kotlin

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val rep: MainRepository
) : ViewModel() {
    val mainIntent = Channel<MainIntent>()

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState: SharedFlow<MainUiState> = _uiState

//    private val _uiState = MutableStateFlow<MainUiState?>(null)
//    val uiState: StateFlow<MainUiState?> = _uiState

    init {
        viewModelScope.launch {
            mainIntent.consumeAsFlow()
                .onEach {
                    when (it) {
                        is MainIntent.Login -> {
                            login(it.phone, it.pwd)
                        }
                    }
                }.catch {

                }.collect()
        }
    }

    private suspend fun login(phone: String, pwd: String) {
        _uiState.emit(MainUiState.Loading)
        try {
            val data = rep.login(phone, pwd)
            _uiState.emit(MainUiState.Success(data))
        } catch (e: Exception) {
            _uiState.emit(MainUiState.Error)
        }
    }
}