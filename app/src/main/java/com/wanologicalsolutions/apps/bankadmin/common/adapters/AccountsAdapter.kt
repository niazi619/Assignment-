package com.wanologicalsolutions.apps.bankadmin.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wanologicalsolutions.apps.bankadmin.databinding.LayoutRvAccountItemBinding
import javax.inject.Inject

class AccountsAdapter @Inject constructor() : RecyclerView.Adapter<AccountsAdapter.AccountView>() {

    private var arrayList: ArrayList<String> = arrayListOf()
    private var isManageView: Boolean = false
    var onItemDeleteListener: ((Int, String) -> Unit)? = null

    fun setManageView(isManageable: Boolean) {
        this.isManageView = isManageable
    }

    fun clearAndAddList(list: ArrayList<String>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountView = AccountView(
        LayoutRvAccountItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: AccountView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = arrayList.size

    inner class AccountView(private val itemBinding: LayoutRvAccountItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(position: Int) {
            arrayList[position].let {
                itemBinding.textViewAccount.text = it
                itemBinding.imageViewDelete.visibility =
                    if (isManageView) View.VISIBLE else View.GONE
                if (isManageView)
                    itemBinding.imageViewDelete.setOnClickListener { _ ->
                        onItemDeleteListener?.invoke(
                            position,
                            it
                        )
                    }
            }
        }

    }
}