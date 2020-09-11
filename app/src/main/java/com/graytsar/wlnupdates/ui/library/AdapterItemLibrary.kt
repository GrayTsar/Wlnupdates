package com.graytsar.wlnupdates.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.graytsar.wlnupdates.ARG_ID_NOVEL
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.ModelLibrary
import com.graytsar.wlnupdates.databinding.ItemLibraryBinding

class AdapterItemLibrary(private val activity: Fragment): ListAdapter<ModelLibrary, ViewHolderLibrary>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLibrary {
        val binding = DataBindingUtil.inflate<ItemLibraryBinding>(
            LayoutInflater.from(activity.context), R.layout.item_library, parent, false)
        return ViewHolderLibrary(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderLibrary, position: Int) {
        holder.binding.lifecycleOwner = activity
        val item = getItem(position)

        holder.binding.textLibraryTitle.text = item.title


            /*
            holder.binding.imageLibraryCover.shapeAppearanceModel = holder.binding.imageLibraryCover.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 15.0f)
                .build()


             */
            holder.binding.imageLibraryCover.load(item.cover) {
                placeholder(R.drawable.ic_app)
                error(R.drawable.ic_app)
            }


        holder.binding.backgroundItemLibrary.setOnClickListener { view ->
            val navHostFragment = (view.context as MainActivity).supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            item.position = 0
            DatabaseService.db?.daoLibrary()!!.update(item)

            val bundle = Bundle()
            bundle.putInt(ARG_ID_NOVEL, item.idWlnupdates)

            navController.navigate(R.id.fragmentNovel, bundle)

        }

        holder.binding.menuLibraryOptions.setOnClickListener { view ->
            val wrapper = ContextThemeWrapper(view.context, R.style.PopupMenuStyle)
            val popupMenu = PopupMenu(wrapper, view)
            popupMenu.inflate(R.menu.menu_item_library_options)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.menuPopLibraryNotification -> {
                        item.isNotificationEnabled = if(item.isNotificationEnabled) {
                            Toast.makeText(view.context, "Disabled", Toast.LENGTH_SHORT).show()
                            false
                        } else {
                            Toast.makeText(view.context, "Enabled", Toast.LENGTH_SHORT).show()
                            true
                        }


                        DatabaseService.db?.daoLibrary()!!.update(item)
                        true
                    }
                    R.id.menuPopLibraryDelete -> {
                        DatabaseService.db?.daoLibrary()!!.delete(item)

                        true
                    }
                    else -> {
                        false
                    }
                }

            }
            popupMenu.show()
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ModelLibrary>(){
            override fun areItemsTheSame(old: ModelLibrary, aNew: ModelLibrary): Boolean {
                return old == aNew
            }

            override fun areContentsTheSame(old: ModelLibrary, aNew: ModelLibrary): Boolean {
                return old.pk == aNew.pk
            }
        }
    }
}

class ViewHolderLibrary(view: View, val binding: ItemLibraryBinding): RecyclerView.ViewHolder(view){

}