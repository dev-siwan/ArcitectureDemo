package com.ksw.presentation.ui.binding

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ksw.domain.common.NetworkState

object BindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["getUri"], requireAll = false)
    fun ImageView.isNullOrEmptyUri(getUri: String?) {
        if (getUri != null) {
            visibility= View.VISIBLE
            Glide.with(this).load(getUri).centerCrop().into(this)
        } else {
            visibility=  View.GONE
            setImageDrawable(null)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["networkState"], requireAll = false)
    fun RecyclerView.networkState(networkState: NetworkState?) {
        visibility = when (networkState) {
            NetworkState.SUCCESS -> {
                View.VISIBLE
            }
            NetworkState.INIT_EMPTY -> {
                View.GONE
            }
            else -> {
                View.VISIBLE
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["networkState"], requireAll = false)
    fun LinearLayout.networkState(networkState: NetworkState?) {
        visibility = when (networkState) {
            NetworkState.INIT_EMPTY -> {
                View.VISIBLE
            }
            else -> {
                View.GONE
            }
        }
    }
}