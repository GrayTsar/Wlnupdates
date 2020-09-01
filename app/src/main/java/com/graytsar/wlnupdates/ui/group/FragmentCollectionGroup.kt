package com.graytsar.wlnupdates.ui.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.graytsar.wlnupdates.ARG_ID_GROUP
import com.graytsar.wlnupdates.databinding.FragmentCollectionGroupBinding

class FragmentCollectionGroup : Fragment() {
    private lateinit var binding: FragmentCollectionGroupBinding
    private val viewModelGroup by viewModels<ViewModelGroup>()
    private lateinit var adapter: FragmentStateGroupAdapter
    private lateinit var viewPager: ViewPager2

    private var idGroup:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idGroup = it.getInt(ARG_ID_GROUP, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionGroupBinding.inflate(inflater, container, false)

        viewPager = binding.includeToolbarCollectionGroup.viewpagerCollectionGroup
        adapter = FragmentStateGroupAdapter(this, viewModelGroup, idGroup)
        viewPager.adapter = adapter
        val tabLayout = binding.includeToolbarCollectionGroup.tabsCollectionGroup
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    "Active Series"
                }
                1 -> {
                    "Feed"
                }
                else -> {
                    "Active Series"
                }
            }
        }.attach()


        return binding.root
    }
}