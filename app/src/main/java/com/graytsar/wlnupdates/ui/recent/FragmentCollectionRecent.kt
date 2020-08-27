package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.graytsar.wlnupdates.R


class FragmentCollectionRecent : Fragment() {
    private lateinit var adapter: FragmentStateRecentAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FragmentStateRecentAdapter(this)
        viewPager = view.findViewById(R.id.viewpagerRecent)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    getString(R.string.tab_recent)
                }
                1 -> {
                    getString(R.string.tab_translated)
                }
                2 -> {
                    getString(R.string.tab_original)
                }
                else -> {
                    getString(R.string.tab_recent)
                }
            }
        }.attach()
    }
}