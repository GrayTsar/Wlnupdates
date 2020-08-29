package com.graytsar.wlnupdates.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemAdvancedGenreBinding
import com.graytsar.wlnupdates.databinding.ItemAdvancedTagBinding
import com.graytsar.wlnupdates.rest.ItemGenre
import com.graytsar.wlnupdates.rest.ItemTag

class AdapterAdvancedSearchTag(private val activity: Fragment): ListAdapter<ItemTag, ViewHolderItemAdvancedSearchTag>(DiffCallbackAdvancedSearchTag()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemAdvancedSearchTag {
        val binding = DataBindingUtil.inflate<ItemAdvancedTagBinding>(
            LayoutInflater.from(activity.context), R.layout.item_advanced_tag, parent, false)
        return ViewHolderItemAdvancedSearchTag(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderItemAdvancedSearchTag, position: Int) {
        holder.binding.lifecycleOwner = activity
        holder.binding.model = getItem(position)
        val item = getItem(position)

        holder.binding.cardAdvancedTag.setOnClickListener { view ->
            val isSelected = item.isSelected.value!!

            item.isSelected.value = !isSelected
        }

    }

}

class ViewHolderItemAdvancedSearchTag(view: View, val binding: ItemAdvancedTagBinding): RecyclerView.ViewHolder(view){

}

class DiffCallbackAdvancedSearchTag: DiffUtil.ItemCallback<ItemTag>(){
    override fun areItemsTheSame(old: ItemTag, aNew: ItemTag): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: ItemTag, aNew: ItemTag): Boolean {
        return (old.id == aNew.id && old.name == aNew.name)
    }
}