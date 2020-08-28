package com.graytsar.wlnupdates.ui.publisher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.graytsar.wlnupdates.databinding.ItemGroupFeedBinding
import com.graytsar.wlnupdates.databinding.ItemPublisherBinding
import com.graytsar.wlnupdates.rest.FeedPaginated
import com.graytsar.wlnupdates.rest.Series

class AdapterPublisher(private val activity: Fragment): ListAdapter<Series, ViewHolderPublisher>(DiffCallbackPublisher()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPublisher {
        val binding = DataBindingUtil.inflate<ItemPublisherBinding>(
            LayoutInflater.from(activity.context), R.layout.item_publisher, parent, false)
        return ViewHolderPublisher(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderPublisher, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)

        holder.binding.textPublisherSeriesName.text = item.name

        holder.binding.backgroundPublisher.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            item.id?.let { id ->
                val bundle = Bundle()
                bundle.putInt(ARG_ID_NOVEL, id)

                navController.navigate(R.id.fragmentNovel, bundle)
            }
        }
    }

}

class ViewHolderPublisher(view: View, val binding: ItemPublisherBinding): RecyclerView.ViewHolder(view){

}

class DiffCallbackPublisher: DiffUtil.ItemCallback<Series>(){
    override fun areItemsTheSame(old: Series, aNew: Series): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: Series, aNew: Series): Boolean {
        return (old.id == aNew.id && old.name == aNew.name)
    }
}