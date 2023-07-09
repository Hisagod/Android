package com.aib.widget

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.aib.base.dialog.TipDialog

fun AppCompatActivity.showDialog(callback: (dialog: DialogFragment) -> Unit) {
    val tipDialog = TipDialog()
    tipDialog.isCancelable = false
    tipDialog.show(supportFragmentManager, "")
    callback(tipDialog)
}