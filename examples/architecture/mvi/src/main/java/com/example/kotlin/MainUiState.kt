package com.example.kotlin

sealed class MainUiState {
    object Loading : MainUiState()
    object Error : MainUiState()
    class Success(val data: UserBean) : MainUiState()
}
