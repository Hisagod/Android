package com.opensource.svgaplayer.utils

import com.blankj.utilcode.util.LogUtils
import net.lingala.zip4j.ZipFile
import java.io.File

object ZipUtils {
    fun unZipByPwd(
        originalPath: String,
        destPath: String
    ) {
        try {
            val zipFile = ZipFile(originalPath, "mikn!o0#5%hya129sd?ks".toCharArray())
                .extractAll(destPath)
        } catch (e: Exception) {
            LogUtils.e(e.message)
        }
    }
}