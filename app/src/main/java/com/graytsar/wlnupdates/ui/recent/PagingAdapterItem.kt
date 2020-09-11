package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.ARG_ID_NOVEL
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemLatestBinding
import com.graytsar.wlnupdates.rest.Item

class PagingAdapterItem(private val activity: Fragment): PagingDataAdapter<Item, ViewHolderItem>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val binding = DataBindingUtil.inflate<ItemLatestBinding>(
            LayoutInflater.from(activity.context), R.layout.item_latest, parent, false)
        return ViewHolderItem(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        holder.binding.lifecycleOwner = activity

        val item = getItem(position)!!

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

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(old: Item, aNew: Item) = old == aNew

            override fun areContentsTheSame(old: Item, aNew: Item): Boolean {
                return (old.chapter == aNew.chapter && old.volume == aNew.volume && old.series?.id == aNew.series?.id)
            }
        }
    }
}

class ViewHolderItem(view: View, val binding: ItemLatestBinding): RecyclerView.ViewHolder(view){

}