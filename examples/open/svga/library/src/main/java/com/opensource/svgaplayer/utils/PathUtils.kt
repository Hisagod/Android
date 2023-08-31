package com.opensource.svgaplayer.utils

import com.opensource.svgaplayer.SVGA
import com.opensource.svgaplayer.utils.log.LogUtils
import java.io.File

object PathUtils {
    private const val TAG = "PathUtils"

    private var svgPath: String? = null

    private var audioPath: String? = null

    fun init() {
        val rootPath = SVGA.app.cacheDir.absolutePath + File.separator + "svga"
        val svgDocument = File(rootPath + File.separator + "svg")
        val audioDocument = File(rootPath + File.separator + "mp3")

        //创建SVGA文件夹
        if (svgDocument.exists()) {
            svgPath = svgDocument.absolutePath
        } else {
            val mkd = svgDocument.mkdirs()

            if (mkd) {
                svgPath = svgDocument.absolutePath
            }

            LogUtils.error(TAG, svgDocument.absolutePath + "创建：" + mkd)
        }

        //创建mp3文件夹
        if (audioDocument.exists()) {
            audioPath = audioDocument.absolutePath
        } else {
            val mkd = audioDocument.mkdirs()

            if (mkd) {
                audioPath = audioDocument.absolutePath
            }

            LogUtils.error(TAG, audioDocument.absolutePath + "创建：" + mkd)
        }
    }

    fun getAudioFile(name: String): File {
        return File(audioPath + File.separator + name + ".mp3")
    }

    fun getSvgFile() {

    }
}