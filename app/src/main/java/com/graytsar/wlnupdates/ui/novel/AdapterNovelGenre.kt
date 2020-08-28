package com.graytsar.wlnupdates.ui.novel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemNovelSimpleExpandedBinding
import com.graytsar.wlnupdates.rest.Genre

class AdapterNovelGenre(private val activity: Fragment): ListAdapter<Genre, ViewHolderNovelSimpleExpanded>(DiffCallbackGenre()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNovelSimpleExpanded {
        val binding = DataBindingUtil.inflate<ItemNovelSimpleExpandedBinding>(
            LayoutInflater.from(activity.context), R.layout.item_novel_simple_expanded, parent, false)
        return ViewHolderNovelSimpleExpanded(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderNovelSimpleExpanded, position: Int) {
        holder.binding.lifecycleOwner = activity

        holder.binding.textSimpleExpanded.text = getItem(position).genre
    }
}

class DiffCallbackGenre: DiffUtil.ItemCallback<Genre>(){
    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.id == newItem.id
    }
}