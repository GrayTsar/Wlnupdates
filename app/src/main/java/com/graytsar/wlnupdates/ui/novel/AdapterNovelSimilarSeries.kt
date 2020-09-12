package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.graytsar.wlnupdates.ARG_ID_NOVEL
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemNovelSimpleExpandedBinding
import com.graytsar.wlnupdates.rest.SimilarSeries

class AdapterNovelSimilarSeries(private val activity: Fragment): ListAdapter<SimilarSeries, ViewHolderNovelSimpleExpanded>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNovelSimpleExpanded {
        val binding = DataBindingUtil.inflate<ItemNovelSimpleExpandedBinding>(
            LayoutInflater.from(activity.context), R.layout.item_novel_simple_expanded, parent, false)
        return ViewHolderNovelSimpleExpanded(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderNovelSimpleExpanded, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)

        holder.binding.textSimpleExpanded.text = item.title
        holder.binding.cardSimpleExpanded.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            item.id?.let {
                val bundle = Bundle()
                bundle.putInt(ARG_ID_NOVEL, it)

                navController.navigate(R.id.fragmentNovel, bundle)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<SimilarSeries>(){
            override fun areItemsTheSame(oldItem: SimilarSeries, newItem: SimilarSeries): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SimilarSeries, newItem: SimilarSeries): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}