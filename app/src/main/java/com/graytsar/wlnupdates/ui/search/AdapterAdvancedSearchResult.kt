package com.graytsar.wlnupdates.ui.search

import android.os.Bundle
import android.text.format.DateUtils
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
import com.graytsar.wlnupdates.databinding.ItemAdvancedSearchResultBinding
import com.graytsar.wlnupdates.databinding.ItemListAdvancedHeaderBinding
import com.graytsar.wlnupdates.rest.data.DataAdvancedSearch
import java.lang.Exception
import java.util.*

class AdapterAdvancedSearchResult(private val activity: Fragment): ListAdapter<DataAdvancedSearch, ViewHolderItemAdvancedSearchResult>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemAdvancedSearchResult {
        val binding = DataBindingUtil.inflate<ItemAdvancedSearchResultBinding>(
            LayoutInflater.from(activity.context), R.layout.item_advanced_search_result, parent, false)
        return ViewHolderItemAdvancedSearchResult(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderItemAdvancedSearchResult, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)

        try {
            val date = item.latestPublished?.toLong()
            date?.let {
                val ago = DateUtils.getRelativeTimeSpanString(it * 1000, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS)
                val str = ago.toString()
                holder.binding.textAdvancedSearchResultDate.text = "Published: $ago"
            }
        } catch (e: Exception) {
            holder.binding.textAdvancedSearchResultDate.text = "Published: ${item.latestPublished}"
        }

        holder.binding.textAdvancedSearchResultTitle.text = item.title
        holder.binding.textAdvancedSearchResultChapterCount.text = "Chapter: ${item.releaseCount}"
        holder.binding.textAdvancedSearchResultRating.text = "Rating: ${String.format("%.2f", item.rating)} (${item.ratingCount})"

        holder.binding.cardAdvancedResult.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val bundle = Bundle()
            bundle.putInt(ARG_ID_NOVEL, item.id!!)

            navController.navigate(R.id.fragmentNovel, bundle)
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<DataAdvancedSearch>(){
            override fun areItemsTheSame(old: DataAdvancedSearch, aNew: DataAdvancedSearch): Boolean {
                return old == aNew
            }

            override fun areContentsTheSame(old: DataAdvancedSearch, aNew: DataAdvancedSearch): Boolean {
                return (old.id == aNew.id && old.title == aNew.title)
            }
        }
    }
}

class ViewHolderItemAdvancedSearchResult(view: View, val binding: ItemAdvancedSearchResultBinding): RecyclerView.ViewHolder(view){

}
