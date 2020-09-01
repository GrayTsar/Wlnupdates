package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_ID_NOVEL_GROUP
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.databinding.FragmentNovelGroupBinding
import com.graytsar.wlnupdates.rest.Tlgroup


class FragmentNovelGroup : Fragment() {
    private lateinit var binding: FragmentNovelGroupBinding
    private val adapter = AdapterNovelGroup(this)

    private var list: ArrayList<Tlgroup>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelableArrayList(ARG_ID_NOVEL_GROUP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNovelGroupBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarSimpleGroup.toolbarSimple
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerNovelGroup.adapter = adapter

        list?.let {
            adapter.submitList(it)
        }

        return binding.root
    }


}