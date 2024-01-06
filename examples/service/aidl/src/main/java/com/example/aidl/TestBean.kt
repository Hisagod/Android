package com.example.aidl

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestBean(var str: String?) : Parcelable {
//    constructor() : this(null)

    fun readFromParcel(i: Parcel) {
        str = i.readString()
    }
}