package com.graytsar.wlnupdates.ui.search

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
import com.graytsar.wlnupdates.databinding.ItemSearchBinding
import com.graytsar.wlnupdates.rest.MatchContent

class PagingAdapterSearch(private val activity: Fragment): PagingDataAdapter<MatchContent, ViewHolderItemSearch>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemSearch {
        val binding = DataBindingUtil.inflate<ItemSearchBinding>(
            LayoutInflater.from(activity.context), R.layout.item_search, parent, false)
        return ViewHolderItemSearch(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderItemSearch, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)!!

        holder.binding.textItemSearch.text = item.name

        holder.binding.cardItemSearch.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val bundle = Bundle()
            bundle.putInt(ARG_ID_NOVEL, item.sid)

            navController.navigate(R.id.fragmentNovel, bundle)
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<MatchContent>(){
            override fun areItemsTheSame(old: MatchContent, aNew: MatchContent): Boolean {
                return old == aNew
            }

            override fun areContentsTheSame(old: MatchContent, aNew: MatchContent): Boolean {
                return (old.name == aNew.name && old.sid == aNew.sid && old.percent == aNew.percent)
            }
        }
    }
}

class ViewHolderItemSearch(view: View, val binding: ItemSearchBinding): RecyclerView.ViewHolder(view){

}