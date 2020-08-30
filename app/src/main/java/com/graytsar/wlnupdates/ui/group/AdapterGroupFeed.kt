package com.graytsar.wlnupdates.ui.group

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.ItemGroupFeedBinding
import com.graytsar.wlnupdates.rest.FeedPaginated

class AdapterGroupFeed(private val activity: Fragment): ListAdapter<FeedPaginated, ViewHolderGroupFeed>(DiffCallbackGroupFeed()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGroupFeed {
        val binding = DataBindingUtil.inflate<ItemGroupFeedBinding>(
            LayoutInflater.from(activity.context), R.layout.item_group_feed, parent, false)
        return ViewHolderGroupFeed(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderGroupFeed, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)
        holder.model = item

        holder.binding.textGroupFeedPublished.text = item.published
        holder.binding.textGroupFeedTitle.text = item.title
        holder.binding.textGroupFeedName.text = item.srcname

        holder.binding.cardItemGroupFeed.setOnClickListener {
            holder.onClick(it)
        }
    }

}

class ViewHolderGroupFeed(view: View, val binding: ItemGroupFeedBinding): RecyclerView.ViewHolder(view){
    var model:FeedPaginated? = null

    fun onClick(view: View) {
        model?.let { model ->
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(model.linkurl)
                }
                view.context.startActivity(intent)
            } catch (e:Exception) {

            }
        }
    }
}

class DiffCallbackGroupFeed: DiffUtil.ItemCallback<FeedPaginated>(){
    override fun areItemsTheSame(old: FeedPaginated, aNew: FeedPaginated): Boolean {
        return old == aNew
    }

    override fun areContentsTheSame(old: FeedPaginated, aNew: FeedPaginated): Boolean {
        return (old.title == aNew.title && old.linkurl == aNew.linkurl)
    }
}