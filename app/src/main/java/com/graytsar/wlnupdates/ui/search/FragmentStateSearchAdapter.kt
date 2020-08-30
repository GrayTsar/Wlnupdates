package com.graytsar.wlnupdates.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentStateSearchAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val arrayTabsFragments = arrayOf(FragmentSearch(), FragmentAdvancedSearch())

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return arrayTabsFragments[position]
    }
}