package com.example.aidl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextBean(val s: String) : Parcelable
