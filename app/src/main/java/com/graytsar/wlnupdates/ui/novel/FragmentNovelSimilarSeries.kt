package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_PARCEL_NOVEL_SIMILAR_SERIES
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.databinding.FragmentNovelSimilarSeriesBinding
import com.graytsar.wlnupdates.rest.SimilarSeries


class FragmentNovelSimilarSeries : Fragment() {
    private lateinit var binding: FragmentNovelSimilarSeriesBinding
    private val adapter = AdapterNovelSimilarSeries(this)
    private var listSimilarSeries:List<SimilarSeries>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listSimilarSeries = it.getParcelableArrayList(ARG_PARCEL_NOVEL_SIMILAR_SERIES)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNovelSimilarSeriesBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarSimilarSeries.toolbarSimilarSeries
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)


        binding.recyclerSimilarSeries.adapter = adapter
        listSimilarSeries?.let {
            adapter.submitList(it)
        }

        return binding.root
    }
}