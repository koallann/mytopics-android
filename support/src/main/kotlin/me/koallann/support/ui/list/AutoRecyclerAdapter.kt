package me.koallann.support.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AutoRecyclerAdapter<E, VH : ViewHolder<E>>(
    private val onCreateViewHolder: (
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ) -> VH
) : RecyclerView.Adapter<VH>() {

    private val items: MutableList<E> by lazy { ArrayList<E>() }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return onCreateViewHolder(layoutInflater, parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemId(position: Int): Long {
        return if (items.size >= position)
            items[position].hashCode().toLong()
        else
            super.getItemId(position)
    }

    fun add(item: E) {
        synchronized(this) {
            items.add(item)
            notifyItemInserted(items.size - 1)
        }
    }

    fun addAll(items: List<E>) {
        synchronized(this) {
            val start = this.items.size
            this.items.addAll(items)
            notifyItemRangeInserted(start, items.size)
        }
    }

    fun remove(item: E) {
        synchronized(this) {
            val index = items.indexOf(item)
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun clear() {
        synchronized(this) {
            items.clear()
            notifyDataSetChanged()
        }
    }

    fun indexOf(item: E): Int {
        return items.indexOf(item)
    }

    fun size(): Int {
        return items.size
    }

}
