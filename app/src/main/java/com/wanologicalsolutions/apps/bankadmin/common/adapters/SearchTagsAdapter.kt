package com.wanologicalsolutions.apps.bankadmin.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wanologicalsolutions.apps.bankadmin.databinding.LayoutRvTagItemBinding
import com.wanologicalsolutions.apps.bankadmin.models.SearchQuery
import javax.inject.Inject

class SearchQueryAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchQueryAdapter.BubbleView>() {

    private val list: ArrayList<SearchQuery> = arrayListOf()

    var onItemClickListener: ((SearchQuery) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BubbleView {
        return BubbleView(
            LayoutRvTagItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BubbleView, position: Int) {
        holder.bind(position)
    }

    fun addItems(itemList: ArrayList<SearchQuery>) {
        list.addAll(itemList)
        notifyDataSetChanged()
    }

    fun clearAndAddItems(itemList: ArrayList<SearchQuery>) {
        list.clear()
        list.addAll(itemList)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    inner class BubbleView(private val itemBinding: LayoutRvTagItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(position: Int) {
            itemBinding.textViewBubbleTitle.text = list[position].query
            itemBinding.root.setOnClickListener {
                onItemClickListener?.invoke(list[position])
            }
        }
    }

}