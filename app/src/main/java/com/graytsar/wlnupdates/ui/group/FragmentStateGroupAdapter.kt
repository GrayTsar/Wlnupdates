package com.graytsar.wlnupdates.ui.group

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentStateGroupAdapter(fragment: Fragment, viewModelGroup: ViewModelGroup, idGroup:Int): FragmentStateAdapter(fragment) {
    private val arrayTabsFragments = arrayOf(FragmentGroupSeries(viewModelGroup, idGroup), FragmentGroupFeed(viewModelGroup, idGroup))

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return arrayTabsFragments[position]
    }
}