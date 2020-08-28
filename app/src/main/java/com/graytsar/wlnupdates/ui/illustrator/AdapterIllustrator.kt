package com.graytsar.wlnupdates.ui.illustrator

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
import com.graytsar.wlnupdates.databinding.ItemIllustratorBinding
import com.graytsar.wlnupdates.databinding.ItemPublisherBinding
import com.graytsar.wlnupdates.rest.Series

class AdapterIllustrator(private val activity: Fragment): ListAdapter<Series, ViewHolderIllustrator>(DiffCallbackIllustrator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderIllustrator {
        val binding = DataBindingUtil.inflate<ItemIllustratorBinding>(
            LayoutInflater.from(activity.context), R.layout.item_illustrator, parent, false)
        return ViewHolderIllustrator(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderIllustrator, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)

        holder.binding.textIllustratorSeriesTitle.text = item.name

        holder.binding.backgroundIllustrator.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            item.id?.let { id ->
                val bundle = Bundle()
                bundle.putInt(ARG_ID_NOVEL, id)

                navController.navigate(R.id.fragmentNovel, bundle)
            }
        }
    }

}

class ViewHolderIllustrator(view: View, val binding: ItemIllustratorBinding): RecyclerView.ViewHolder(view){

}

class DiffCallbackIllustrator: DiffUtil.ItemCallback<Series>(){
    override fun areItemsTheSame(old: Series, aNew: Series): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: Series, aNew: Series): Boolean {
        return (old.id == aNew.id && old.name == aNew.name)
    }
}