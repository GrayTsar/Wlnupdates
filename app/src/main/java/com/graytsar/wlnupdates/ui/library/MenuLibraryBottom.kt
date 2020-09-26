package com.graytsar.wlnupdates.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.graytsar.wlnupdates.ARG_PARCEL_LIBRARY_ITEM
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.ModelLibrary
import com.graytsar.wlnupdates.databinding.FragmentMenuLibraryBottomBinding

class MenuLibraryBottom: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuLibraryBottomBinding
    private lateinit var switchNotification: SwitchMaterial
    private lateinit var menuDelete: View
    private lateinit var menuLibraryClose: View

    private var item: ModelLibrary? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuLibraryBottomBinding.inflate(inflater, container, false)
        arguments?.let {
            item = it.getParcelable<ModelLibrary>(ARG_PARCEL_LIBRARY_ITEM)
        }

        switchNotification = binding.menuLibraryNotificationSwitch
        menuDelete = binding.menuLibraryDelete
        menuLibraryClose = binding.menuLibraryClose

        item?.let { item ->
            if(item.isNotificationEnabled) {
                switchNotification.isChecked = true
            }

            switchNotification.setOnClickListener {
                if(it is SwitchMaterial) {
                    item.isNotificationEnabled = it.isChecked
                    DatabaseService.db?.daoLibrary()?.update(item)
                }
            }

            menuDelete.setOnClickListener {
                DatabaseService.db?.daoLibrary()?.delete(item)
                this.dismiss()
            }
        }

        menuLibraryClose.setOnClickListener {
            this.dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        BottomSheetBehavior.from(binding.root.parent as View).state = BottomSheetBehavior.STATE_EXPANDED
    }
}