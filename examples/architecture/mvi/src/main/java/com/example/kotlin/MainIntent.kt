package com.example.kotlin

sealed class MainIntent {
    class Login(val phone: String, val pwd: String) : MainIntent()
}
