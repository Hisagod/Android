package com.example.aidl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class SenderBean<D>(val request: String, val data: @RawValue D?) : Parcelable
