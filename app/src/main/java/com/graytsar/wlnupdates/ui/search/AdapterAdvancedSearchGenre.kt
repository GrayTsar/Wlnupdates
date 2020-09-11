package com.graytsar.wlnupdates.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemListAdvancedGenreBinding
import com.graytsar.wlnupdates.databinding.ItemListAdvancedHeaderBinding
import com.graytsar.wlnupdates.rest.ItemGenre

class AdapterAdvancedSearchGenre(private val activity: Fragment): PagingDataAdapter<ItemGenre, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                val binding = DataBindingUtil.inflate<ItemListAdvancedHeaderBinding>(LayoutInflater.from(activity.context), R.layout.item_list_advanced_header, parent, false)
                return ViewHolderBaseAdvancedSearch(binding.root, binding)
            }
            TYPE_CONTENT -> {
                val binding = DataBindingUtil.inflate<ItemListAdvancedGenreBinding>(LayoutInflater.from(activity.context), R.layout.item_list_advanced_genre, parent, false)
                return ViewHolderItemAdvancedSearchGenre(binding.root, binding)
            }
        }

        val binding = DataBindingUtil.inflate<ItemListAdvancedGenreBinding>(LayoutInflater.from(activity.context), R.layout.item_list_advanced_genre, parent, false)
        return ViewHolderItemAdvancedSearchGenre(binding.root, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderBaseAdvancedSearch -> {
                holder.binding.textAdvancedHeaderTitle.text = "Genre"
            }
            is ViewHolderItemAdvancedSearchGenre -> {
                holder.binding.lifecycleOwner = activity
                val item = getItem(position)!!
                holder.binding.model = item


                holder.binding.root.setOnClickListener { view ->
                    val isSelected = item.isSelected.value!!

                    item.isSelected.value = !isSelected
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) {
            TYPE_HEADER
        } else {
            TYPE_CONTENT
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ItemGenre>(){
            override fun areItemsTheSame(old: ItemGenre, aNew: ItemGenre): Boolean {
                return old == aNew
            }

            override fun areContentsTheSame(old: ItemGenre, aNew: ItemGenre): Boolean {
                return (old.id == aNew.id && old.name == aNew.name)
            }
        }

        private const val TYPE_HEADER = 1
        private const val TYPE_CONTENT = 2
    }
}

class ViewHolderItemAdvancedSearchGenre(view: View, val binding: ItemListAdvancedGenreBinding): RecyclerView.ViewHolder(view){

}