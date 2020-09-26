package com.graytsar.wlnupdates.ui.novel

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemLatestBinding
import com.graytsar.wlnupdates.rest.Release
import com.graytsar.wlnupdates.ui.recent.ViewHolderItem

class AdapterNovelChapter(private val activity: Fragment): ListAdapter<Release, ViewHolderItem>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val binding = DataBindingUtil.inflate<ItemLatestBinding>(
            LayoutInflater.from(activity.context), R.layout.item_latest, parent, false)
        return ViewHolderItem(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        holder.binding.lifecycleOwner = activity

        val item = getItem(position)

        item.volume?.let {
            holder.binding.textLatestVolume.text = "${activity.getString(R.string.item_latest_volume)} ${item.volume}"
        } ?: let {
            //if volume is null
            holder.binding.textLatestVolume.text = "${activity.getString(R.string.item_latest_volume)} 0"
        }
        item.chapter?.let {
            holder.binding.textLatestChapter.text = "${activity.getString(R.string.item_latest_chapter)} ${item.chapter}"
        } ?:let {
            //if chapter is null
            holder.binding.textLatestChapter.text = "${activity.getString(R.string.item_latest_chapter)} 0"
        }
        item.postfix?.let {
            if(it.isEmpty()){
                holder.binding.textLatestPostfix.visibility = View.GONE
            } else {
                holder.binding.textLatestPostfix.text = it
            }
        }

        holder.binding.textLatestTitle.text = item.series?.name
        holder.binding.textLatestGroup.text = item.tlgroup?.name

        holder.binding.latestBackground.setOnClickListener { view ->
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(item.srcurl)
                }
                activity.startActivity(intent)
            } catch (e:Exception) {

            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Release>(){
            override fun areItemsTheSame(oldItem: Release, newItem: Release): Boolean {
                return oldItem.srcurl == newItem.srcurl
            }

            override fun areContentsTheSame(oldItem: Release, newItem: Release): Boolean {
                return (oldItem.chapter == newItem.chapter && oldItem.volume == newItem.volume && oldItem.series?.id == newItem.series?.id)
            }
        }
    }
}