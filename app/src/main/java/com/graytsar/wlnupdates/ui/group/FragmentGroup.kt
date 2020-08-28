package com.graytsar.wlnupdates.ui.group

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_ID_GROUP
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentGroupBinding
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestGroup
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentGroup : Fragment() {
    private lateinit var binding:FragmentGroupBinding
    private val viewModelGroup by viewModels<ViewModelGroup>()
    private val adapterGroupFeed = AdapterGroupFeed(this)
    private val adapterGroupSeries = AdapterGroupSeries(this)

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
        binding = FragmentGroupBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarGroup.toolbarGroup
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerGroupSeries.adapter = adapterGroupSeries
        binding.recyclerGroupFeed.adapter = adapterGroupSeries

        GlobalScope.launch {
            if(idGroup > 0) {
                viewModelGroup.getDataGroup(id)
            }
        }.invokeOnCompletion {
            viewModelGroup.activeSeries?.let { map ->
                val mapList = ArrayList<Map.Entry<String, String>>()
                map.forEach {
                    mapList.add(it)
                }
                adapterGroupSeries.submitList(mapList)
            }
            viewModelGroup.feedPaginated?.let {
                adapterGroupFeed.submitList(it.toMutableList())
            }
        }

        return binding.root
    }

}