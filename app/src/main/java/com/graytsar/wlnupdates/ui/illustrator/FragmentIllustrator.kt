package com.graytsar.wlnupdates.ui.illustrator

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
import com.graytsar.wlnupdates.ARG_ID_ILLUSTRATOR
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentIllustratorBinding
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestIllustrator
import com.graytsar.wlnupdates.rest.request.RequestPublisher
import kotlinx.android.synthetic.main.toolbar_illustrator.view.*
import kotlinx.android.synthetic.main.toolbar_publisher.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentIllustrator : Fragment() {
    private lateinit var binding: FragmentIllustratorBinding
    private val viewModelIllustrator by viewModels<ViewModelIllustrator>()
    private val adapterIllustrator = AdapterIllustrator(this)

    private var idIllustrator:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idIllustrator = it.getInt(ARG_ID_ILLUSTRATOR, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIllustratorBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarIllustrator.toolbarIllustrator
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerIllustrator.adapter = adapterIllustrator

        GlobalScope.launch {
            if(idIllustrator > 0) {
                val result = RestService.restService.getIllustrator(RequestIllustrator(idIllustrator)).execute().body()
                result?.let {responsePublisher ->
                    responsePublisher.data?.series?.let { list ->
                        viewModelIllustrator.list = list
                    }
                }
            }
        }.invokeOnCompletion {
            lifecycleScope.launch {
                adapterIllustrator.submitList(viewModelIllustrator.list?.toMutableList())
            }
        }

        return binding.root
    }

}