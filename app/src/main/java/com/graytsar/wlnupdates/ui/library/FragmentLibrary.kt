package com.graytsar.wlnupdates.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.ModelLibrary
import com.graytsar.wlnupdates.databinding.FragmentLibraryBinding
import kotlinx.coroutines.launch

class FragmentLibrary : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val adapter = AdapterItemLibrary(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarLibrary.toolbarLibrary
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        val flexBoxLayoutManager = FlexboxLayoutManager(context)
        flexBoxLayoutManager.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            flexWrap = FlexWrap.WRAP
        }

        binding.recyclerLibrary.layoutManager = flexBoxLayoutManager
        binding.recyclerLibrary.adapter = adapter

        var array:LiveData<List<ModelLibrary>>? = null
        lifecycleScope.launch {
            array = DatabaseService.db?.daoLibrary()!!.selectAll()
        }.invokeOnCompletion {
            array?.observe(viewLifecycleOwner) { list ->
                val sortedList = list.sortedBy {
                    it.position
                }

                var i:Long = 1
                sortedList.forEach { model ->
                    model.position = i
                    i++
                }


                adapter.submitList(sortedList)
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        adapter.currentList.forEach {
            DatabaseService.db?.daoLibrary()!!.update(it)
        }

    }
}