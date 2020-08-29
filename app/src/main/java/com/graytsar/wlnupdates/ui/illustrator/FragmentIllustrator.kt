package com.graytsar.wlnupdates.ui.illustrator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_ID_ILLUSTRATOR
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentIllustratorBinding
import com.graytsar.wlnupdates.rest.MatchContent
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestIllustrator
import com.graytsar.wlnupdates.rest.request.RequestPublisher
import com.graytsar.wlnupdates.rest.request.RequestSearch
import com.graytsar.wlnupdates.rest.response.ResponseIllustrator
import com.graytsar.wlnupdates.rest.response.ResponseSearch
import kotlinx.android.synthetic.main.toolbar_illustrator.view.*
import kotlinx.android.synthetic.main.toolbar_publisher.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        binding.lifecycleOwner = this
        binding.viewModelIllustrator = viewModelIllustrator

        val toolbar: Toolbar = binding.includeToolbarIllustrator.toolbarIllustrator
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerIllustrator.adapter = adapterIllustrator

        viewModelIllustrator.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarIllustrator.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        viewModelIllustrator.list.observe(viewLifecycleOwner, {
            adapterIllustrator.submitList(it)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(idIllustrator > 0) {
            viewModelIllustrator.getDataIllustrator(idIllustrator)
        }
    }
}