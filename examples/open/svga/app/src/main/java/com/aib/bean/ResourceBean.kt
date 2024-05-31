package com.aib.bean

import androidx.annotation.RawRes

data class ResourceBean(
    val desc: String,
    @RawRes
    val resource: Int
)