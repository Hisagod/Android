package com.aib.activity.resource

import androidx.annotation.RawRes

data class ResourceBean(
    val desc: String,
    @RawRes
    val resource: Int
)