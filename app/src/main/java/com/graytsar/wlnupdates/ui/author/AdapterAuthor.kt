package com.graytsar.wlnupdates.ui.author

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemAuthorBinding
import com.graytsar.wlnupdates.rest.SeriesAuthor

class AdapterAuthor(private val activity: Fragment): ListAdapter<SeriesAuthor, ViewHolderAuthor>(DiffCallbackAuthor()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAuthor {
        val binding = DataBindingUtil.inflate<ItemAuthorBinding>(
            LayoutInflater.from(activity.context), R.layout.item_author, parent, false)
        return ViewHolderAuthor(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderAuthor, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)

        holder.binding.textAuthor.text = item.title
    }

}

class ViewHolderAuthor(view: View, val binding: ItemAuthorBinding): RecyclerView.ViewHolder(view){

}

class DiffCallbackAuthor: DiffUtil.ItemCallback<SeriesAuthor>(){
    override fun areItemsTheSame(old: SeriesAuthor, aNew: SeriesAuthor): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: SeriesAuthor, aNew: SeriesAuthor): Boolean {
        return (old.id == aNew.id && old.title == aNew.title)
    }
}