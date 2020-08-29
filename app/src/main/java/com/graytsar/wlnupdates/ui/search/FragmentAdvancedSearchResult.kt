package com.graytsar.wlnupdates.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentAdvancedSearchResultBinding

class FragmentAdvancedSearchResult : Fragment() {
    private lateinit var binding: FragmentAdvancedSearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvancedSearchResultBinding.inflate(inflater, container, false)


        return binding.root
    }
}