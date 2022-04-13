package com.wanologicalsolutions.apps.bankadmin.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wanologicalsolutions.apps.bankadmin.R
import com.wanologicalsolutions.apps.bankadmin.common.enums.Action
import com.wanologicalsolutions.apps.bankadmin.databinding.LayoutRvClientItemBinding
import com.wanologicalsolutions.apps.bankadmin.models.Client
import javax.inject.Inject

class ResultsComparator : DiffUtil.ItemCallback<Client>() {
    override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem.firstName == newItem.firstName
    }
}

class ClientPaginationAdapter @Inject constructor() :
    PagingDataAdapter<Client, ClientPaginationAdapter.ClientView>(ResultsComparator()) {

    var onItemClickCallback: ((Int, Client, Action) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientPaginationAdapter.ClientView {
        return ClientView(
            LayoutRvClientItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ClientPaginationAdapter.ClientView,
        position: Int
    ) {
        holder.bind(position)
    }


    inner class ClientView(private val itemBinding: LayoutRvClientItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(position: Int) {
            getItem(position)?.let {
                itemBinding.textViewName.text = "%s %s".format(it.firstName, it.lastName)
                itemBinding.textViewEmail.text = it.email
                itemBinding.textViewDiscription.text = "%s, %s".format(it.city, it.country)
                Glide.with(itemBinding.root.context)
                    .load(it.profilePhoto)
                    .placeholder(R.drawable.img_placeholder_client)
                    .into(itemBinding.imageViewUserProfile)
                itemBinding.root.setOnClickListener { _ ->
                    onItemClickCallback?.invoke(
                        position,
                        it,
                        Action.ACTION_VIEW
                    )
                }
                itemBinding.imageViewManage.setOnClickListener { view ->
                    showPopup(view, position, it)
                }
            }
        }

        private fun showPopup(
            view: View,
            position: Int,
            client: Client
        ) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.client_manage_options)
            // popup.menu.findItem(R.id.publish)?.isVisible = true
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit -> {
                        onItemClickCallback?.invoke(
                            position,
                            client,
                            Action.ACTION_EDIT
                        )
                        true
                    }
                    R.id.delete -> {
                        onItemClickCallback?.invoke(
                            position,
                            client,
                            Action.ACTION_DELETE
                        )
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}
