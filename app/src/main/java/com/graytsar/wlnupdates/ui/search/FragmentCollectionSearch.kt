package com.graytsar.wlnupdates.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentCollectionSearchBinding


class FragmentCollectionSearch : Fragment() {
    private lateinit var binding: FragmentCollectionSearchBinding
    private lateinit var adapter: FragmentStateSearchAdapter
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
        binding = FragmentCollectionSearchBinding.inflate(inflater, container, false)

        adapter = FragmentStateSearchAdapter(this)
        viewPager = binding.includeToolbarCollectionSearch.viewpagerCollectionSearch
        viewPager.adapter = adapter
        val tabLayout = binding.includeToolbarCollectionSearch.tabsCollectionSearch
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    getString(R.string.tab_search)
                }
                1 -> {
                    getString(R.string.tab_search_advanced)
                }
                else -> {
                    getString(R.string.tab_search)
                }
            }
        }.attach()


        return binding.root
    }

}