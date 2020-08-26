package com.graytsar.wlnupdates.ui.recent

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentStateRecentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val arrayTabsFragments = arrayOf(FragmentRecent(), FragmentTranslated(), FragmentOriginals())

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return arrayTabsFragments[position]
    }
}