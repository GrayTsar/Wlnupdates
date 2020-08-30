package com.graytsar.wlnupdates.ui.group

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
import com.graytsar.wlnupdates.databinding.ItemGroupSeriesBinding
import java.lang.Exception

class AdapterGroupSeries(private val activity: Fragment): ListAdapter<Map.Entry<String, String>, ViewHolderGroupSeries>(DiffCallbackGroupSeries()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGroupSeries {
        val binding = DataBindingUtil.inflate<ItemGroupSeriesBinding>(
            LayoutInflater.from(activity.context), R.layout.item_group_series, parent, false)
        return ViewHolderGroupSeries(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderGroupSeries, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)
        holder.model = item

        holder.binding.textGroupSeries.text = item.value
        holder.binding.cardItemGroupSeries.setOnClickListener {
            holder.onClick(it)
        }

    }

}

class ViewHolderGroupSeries(view: View, val binding: ItemGroupSeriesBinding): RecyclerView.ViewHolder(view){
    var model:Map.Entry<String, String>? = null

    fun onClick(view: View) {
        model?.let { model ->
            try {
                val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController: NavController = navHostFragment.navController

                model.key.toIntOrNull()?.let { id ->
                    val bundle = Bundle()
                    bundle.putInt(ARG_ID_NOVEL, id)

                    navController.navigate(R.id.fragmentNovel, bundle)
                }
            } catch (e:Exception) {

            }
        }
    }
}

class DiffCallbackGroupSeries: DiffUtil.ItemCallback<Map.Entry<String, String>>(){
    override fun areItemsTheSame(old: Map.Entry<String, String>, aNew: Map.Entry<String, String>): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: Map.Entry<String, String>, aNew: Map.Entry<String, String>): Boolean {
        return (old.key == aNew.key && old.value == aNew.value)
    }
}