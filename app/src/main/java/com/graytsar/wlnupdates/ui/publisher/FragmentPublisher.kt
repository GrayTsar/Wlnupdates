package com.graytsar.wlnupdates.ui.publisher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.ARG_ID_PUBLISHER
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentPublisherBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FragmentPublisher : Fragment() {
    private lateinit var binding: FragmentPublisherBinding
    private val viewModelPublisher by viewModels<ViewModelPublisher>()
    private val adapterPublisher = PagingAdapterPublisher(this)

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
        binding.lifecycleOwner = this
        binding.viewModelPublisher = viewModelPublisher

        val toolbar: Toolbar = binding.includeToolbarPublisher.toolbarPublisher
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerPublisher.adapter = adapterPublisher

        viewModelPublisher.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarPublisher.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        viewModelPublisher.name.observe(viewLifecycleOwner) {
            toolbar.title = it
        }

        viewModelPublisher.errorResponsePublisher.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelPublisher.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelPublisher.errorServerPublisher.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        })

        lifecycleScope.launch {
            adapterPublisher.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelPublisher.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelPublisher.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelPublisher.setLoadingIndicator(false)
                    }
                    else -> {
                        viewModelPublisher.setLoadingIndicator(false)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(idPublisher > 0) {
            viewModelPublisher.createPager(idPublisher)

            lifecycleScope.launch {
                viewModelPublisher.pagerPublisher?.collectLatest { pagingData ->
                    adapterPublisher.submitData(pagingData)
                }
            }
        }
    }

    private fun showErrorDialog(title:String, message:String?){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.alert_dialog_ok), null)
            .show()
    }
}