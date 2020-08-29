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
import com.graytsar.wlnupdates.rest.ItemGenre

class AdapterAdvancedSearchGenre(private val activity: Fragment): ListAdapter<ItemGenre, ViewHolderItemAdvancedSearchGenre>(DiffCallbackAdvancedSearchGenre()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemAdvancedSearchGenre {
        val binding = DataBindingUtil.inflate<ItemAdvancedGenreBinding>(
            LayoutInflater.from(activity.context), R.layout.item_advanced_genre, parent, false)
        return ViewHolderItemAdvancedSearchGenre(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderItemAdvancedSearchGenre, position: Int) {
        holder.binding.lifecycleOwner = activity
        holder.binding.model = getItem(position)
        val item = getItem(position)

        holder.binding.cardAdvancedGenre.setOnClickListener { view ->
            val isSelected = item.isSelected.value!!

            item.isSelected.value = !isSelected
        }

    }

}

class ViewHolderItemAdvancedSearchGenre(view: View, val binding: ItemAdvancedGenreBinding): RecyclerView.ViewHolder(view){

}

class DiffCallbackAdvancedSearchGenre: DiffUtil.ItemCallback<ItemGenre>(){
    override fun areItemsTheSame(old: ItemGenre, aNew: ItemGenre): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: ItemGenre, aNew: ItemGenre): Boolean {
        return (old.id == aNew.id && old.name == aNew.name)
    }
}