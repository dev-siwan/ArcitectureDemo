package com.ksw.base.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes


fun Context.showShortToast(@StringRes resId: Int) = showToast(resId, Toast.LENGTH_LONG)
fun Context.showLongToast(@StringRes resId: Int) = showToast(resId, Toast.LENGTH_SHORT)

fun Context.showShortToast(text: String) = showToast(text, Toast.LENGTH_LONG)
fun Context.showLongToast(text: String) = showToast(text, Toast.LENGTH_SHORT)

fun Context.showToast(text: String, duration: Int) = Toast.makeText(this, text, duration).show()
fun Context.showToast(@StringRes resId: Int, duration: Int) =
    Toast.makeText(this, resId, duration).show()

fun Context.showListDialog(list: Array<String>, title: String?, action: (Int) -> Unit) {
    val builder = AlertDialog.Builder(this)
    title?.let {
        builder.setTitle(it)
    }
    builder.setItems(list) { _, position ->
        action(position)
    }
    val dialog = builder.create()

    dialog?.run {
        show()
    }
}

fun Context.showConfirmDialog(
    title: String?,
    message: String?,
    positiveTitle: String,
    negativeTitle: String,
    action: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
    title?.let {
        builder.setTitle(it)
    }
    message?.let {
        builder.setMessage(it)
    }
    builder.setTitle(title).setMessage(message).setPositiveButton(positiveTitle) { _, _ ->
        action.invoke()
    }.setNegativeButton(negativeTitle) { dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()

    dialog?.run {
        show()
    }
}


