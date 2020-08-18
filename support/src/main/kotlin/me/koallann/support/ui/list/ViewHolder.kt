package me.koallann.support.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class ViewHolder<T>(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var item: T? = null

    abstract fun onBind(item: T)

    fun bind(item: T) {
        this.item = item
        onBind(item)
    }

}
