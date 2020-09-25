package me.koallann.support.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T>(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(item: T)

    fun bind(item: T) = onBind(item)

}
