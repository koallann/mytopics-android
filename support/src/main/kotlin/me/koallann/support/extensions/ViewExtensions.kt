package me.koallann.support.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:visible")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
