package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.ARG_ID_NOVEL
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemLatestBinding
import com.graytsar.wlnupdates.rest.Item
import java.util.*
import kotlin.collections.ArrayList

class AdapterItem(private val activity: Fragment): ListAdapter<Item, ViewHolderItem>(DiffCallbackItem()) {
    var pattern:String = ""

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
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            item.series?.id?.let {
                val bundle = Bundle()
                bundle.putInt(ARG_ID_NOVEL, it)

                navController.navigate(R.id.fragmentNovel, bundle)
            }
        }
    }

    override fun submitList(list: MutableList<Item>?) {
        val filteredList = ArrayList<Item>()
        val defaultLocale = Locale.getDefault()
        val query = pattern.toLowerCase(defaultLocale).trim()


        if(query.isNotEmpty()){
            list?.forEach { item ->
                var name:String = ""
                var postfix:String = ""
                var tlGroup:String = ""

                item.series?.name?.let {
                    name = it.toLowerCase(defaultLocale)
                }
                item.postfix?.let {
                    postfix = it.toLowerCase(defaultLocale)
                }
                item.tlgroup?.name?.let {
                    tlGroup = it.toLowerCase(defaultLocale)
                }

                if( name.contains(query) || postfix.contains(query) || tlGroup.contains(query)) {
                    filteredList.add(item)
                }
            }
            super.submitList(filteredList)
        } else {
            super.submitList(list)
        }
    }
}

class ViewHolderItem(view: View, val binding:ItemLatestBinding): RecyclerView.ViewHolder(view){

}

class DiffCallbackItem: DiffUtil.ItemCallback<Item>(){
    override fun areItemsTheSame(old: Item, aNew: Item): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: Item, aNew: Item): Boolean {
        return (old.chapter == aNew.chapter && old.volume == aNew.volume && old.series?.id == aNew.series?.id)
    }
}