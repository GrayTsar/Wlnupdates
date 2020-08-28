package com.graytsar.wlnupdates.ui.publisher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_ID_PUBLISHER
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentPublisherBinding
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestPublisher
import kotlinx.android.synthetic.main.toolbar_publisher.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FragmentPublisher : Fragment() {
    private lateinit var binding: FragmentPublisherBinding
    private val viewModelPublisher by viewModels<ViewModelPublisher>()
    private val adapterPublisher = AdapterPublisher(this)

    private var idPublisher = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idPublisher = it.getInt(ARG_ID_PUBLISHER, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPublisherBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarPublisher.toolbarPublisher
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerPublisher.adapter = adapterPublisher

        GlobalScope.launch {
            if(idPublisher > 0) {
                val result = RestService.restService.getPublisher(RequestPublisher(idPublisher)).execute().body()
                result?.let {responsePublisher ->
                    responsePublisher.data?.series?.let { list ->
                        viewModelPublisher.list = list
                    }
                }
            }
        }.invokeOnCompletion {
            lifecycleScope.launch {
                adapterPublisher.submitList(viewModelPublisher.list?.toMutableList())
            }
        }

        return binding.root
    }


}