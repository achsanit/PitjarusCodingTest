package com.achsanit.pitjaruscodingtest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.achsanit.pitjaruscodingtest.databinding.ItemStoreBinding
import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity
import com.achsanit.pitjaruscodingtest.util.extension.makeGone
import com.achsanit.pitjaruscodingtest.util.extension.makeVisible

class ListStoreAdapter(
    private val onItemClick: (StoreEntity) -> Unit
) : RecyclerView.Adapter<ListStoreAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ItemStoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoreEntity) {
            with(binding) {
                tvStoreName.text = data.storeName

                root.setOnClickListener {
                    onItemClick(data)
                }

                if (data.visited) {
                    tvPerfectStore.makeVisible()
                    ivVisited.makeVisible()
                } else {
                    tvPerfectStore.makeGone()
                    ivVisited.makeGone()
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<StoreEntity>() {
        override fun areItemsTheSame(oldItem: StoreEntity, newItem: StoreEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoreEntity, newItem: StoreEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(data: List<StoreEntity>) {
        differ.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoreBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = differ.currentList[position]
        holder.bind(currentData)
    }

}