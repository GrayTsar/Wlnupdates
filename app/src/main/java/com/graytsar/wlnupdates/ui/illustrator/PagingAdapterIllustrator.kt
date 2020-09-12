package com.graytsar.wlnupdates.ui.illustrator

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
import com.graytsar.wlnupdates.databinding.ItemIllustratorBinding
import com.graytsar.wlnupdates.rest.SeriesTitle

class PagingAdapterIllustrator(private val activity: Fragment): PagingDataAdapter<SeriesTitle, ViewHolderIllustrator>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderIllustrator {
        val binding = DataBindingUtil.inflate<ItemIllustratorBinding>(
            LayoutInflater.from(activity.context), R.layout.item_illustrator, parent, false)
        return ViewHolderIllustrator(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderIllustrator, position: Int) {
        holder.binding.lifecycleOwner = activity
        holder.model = getItem(position)

        holder.binding.textIllustratorSeriesTitle.text = holder.model!!.title
        holder.binding.cardItemIllustrator.setOnClickListener { view ->
            holder.onClick(view)
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<SeriesTitle>(){
            override fun areItemsTheSame(old: SeriesTitle, aNew: SeriesTitle): Boolean {
                return old == aNew
            }

            override fun areContentsTheSame(old: SeriesTitle, aNew: SeriesTitle): Boolean {
                return (old.id == aNew.id && old.title == aNew.title)
            }
        }
    }
}

class ViewHolderIllustrator(view: View, val binding: ItemIllustratorBinding): RecyclerView.ViewHolder(view){
    var model:SeriesTitle? = null

    fun onClick(view: View) {
        val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        model?.id?.let { id ->
            val bundle = Bundle()
            bundle.putInt(ARG_ID_NOVEL, id)

            navController.navigate(R.id.fragmentNovel, bundle)
        }
    }
}