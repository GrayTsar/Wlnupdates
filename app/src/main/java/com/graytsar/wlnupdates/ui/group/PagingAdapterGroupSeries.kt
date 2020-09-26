package com.graytsar.wlnupdates.ui.group

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
import com.graytsar.wlnupdates.databinding.ItemGroupSeriesBinding
import com.graytsar.wlnupdates.rest.ModelActiveSeries

class PagingAdapterGroupSeries(private val activity: Fragment): PagingDataAdapter<ModelActiveSeries, ViewHolderGroupSeries>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGroupSeries {
        val binding = DataBindingUtil.inflate<ItemGroupSeriesBinding>(
            LayoutInflater.from(activity.context), R.layout.item_group_series, parent, false)
        return ViewHolderGroupSeries(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderGroupSeries, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)!!
        holder.model = item

        holder.binding.textGroupSeries.text = item.title
        holder.binding.cardItemGroupSeries.setOnClickListener {
            holder.onClick(it)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ModelActiveSeries>(){
            override fun areItemsTheSame(old: ModelActiveSeries, aNew: ModelActiveSeries): Boolean {
                return old.id == aNew.id
            }

            override fun areContentsTheSame(old: ModelActiveSeries, aNew: ModelActiveSeries): Boolean {
                return old.title == aNew.title
            }
        }
    }
}

class ViewHolderGroupSeries(view: View, val binding: ItemGroupSeriesBinding): RecyclerView.ViewHolder(view){
    var model:ModelActiveSeries? = null

    fun onClick(view: View) {
        model?.let { model ->
            try {
                val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController: NavController = navHostFragment.navController

                model.id.toIntOrNull()?.let { id ->
                    val bundle = Bundle()
                    bundle.putInt(ARG_ID_NOVEL, id)

                    navController.navigate(R.id.fragmentNovel, bundle)
                }
            } catch (e:Exception) {

            }
        }
    }
}