package com.graytsar.wlnupdates.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_PARCEL_ADVANCED_SEARCH_RESULT
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.databinding.FragmentAdvancedSearchResultBinding
import com.graytsar.wlnupdates.rest.data.DataAdvancedSearch

class FragmentAdvancedSearchResult : Fragment() {
    private lateinit var binding: FragmentAdvancedSearchResultBinding
    private val adapter = AdapterAdvancedSearchResult(this)

    private var list: ArrayList<DataAdvancedSearch>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelableArrayList(ARG_PARCEL_ADVANCED_SEARCH_RESULT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvancedSearchResultBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarAdvancedResult.toolbarAdvancedResult
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerAdvancedResult.adapter = adapter

        list?.let {
            adapter.submitList(it)
        }

        return binding.root
    }
}