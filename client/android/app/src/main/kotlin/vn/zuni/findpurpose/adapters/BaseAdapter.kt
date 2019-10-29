package vn.zuni.findpurpose.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import vn.zuni.findpurpose.extensions.inflate
import vn.zuni.findpurpose.extensions.onClick

/**
 *
 * Created by namnd on 10-Jan-18.
 */
abstract class BaseAdapter<ITEM> constructor(protected var itemList: List<ITEM>,
                                             private val layoutResId: Int)
    : RecyclerView.Adapter<BaseAdapter.Holder>() {
    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = parent inflate   layoutResId
        val viewHolder = Holder(view)
        val itemView = viewHolder.itemView
        itemView.onClick {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(itemView, adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseAdapter.Holder, position: Int) {
        val item = itemList[position]
        holder.itemView.bind(item, position)
    }

    open fun setData(itemList: List<ITEM>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun addAll(itemList: List<ITEM>) {
        val startPosition = this.itemList.size
        this.itemList.toMutableList().addAll(itemList)
        notifyItemRangeInserted(startPosition, this.itemList.size)
    }

    fun add(item: ITEM) {
        itemList.toMutableList().add(item)
        notifyItemInserted(itemList.size)
    }

    fun remove(position: Int) {
        itemList.toMutableList().removeAt(position)
        notifyItemRemoved(position)
    }

    final override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        onViewRecycled(holder.itemView)
    }

    protected open fun onViewRecycled(itemView: View) {
    }

    protected open fun onItemClick(itemView: View, position: Int) {
    }

    protected open fun View.bind(item: ITEM) {
    }

    protected open fun View.bind(item: ITEM, position: Int) {
        bind(item)
    }
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}