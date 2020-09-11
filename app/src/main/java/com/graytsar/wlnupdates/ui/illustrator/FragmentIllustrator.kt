package com.graytsar.wlnupdates.ui.illustrator

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
import com.graytsar.wlnupdates.ARG_ID_ILLUSTRATOR
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentIllustratorBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentIllustrator : Fragment() {
    private lateinit var binding: FragmentIllustratorBinding
    private val viewModelIllustrator by viewModels<ViewModelIllustrator>()
    private val adapterIllustrator = PagingAdapterIllustrator(this)

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

        viewModelIllustrator.name.observe(viewLifecycleOwner) {
            toolbar.title = it
        }

        viewModelIllustrator.errorResponseIllustrator.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelIllustrator.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelIllustrator.errorServerIllustrator.observe(viewLifecycleOwner) {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        }

        lifecycleScope.launch {
            adapterIllustrator.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelIllustrator.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelIllustrator.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelIllustrator.setLoadingIndicator(false)
                    }
                    else -> {
                        viewModelIllustrator.setLoadingIndicator(false)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(idIllustrator > 0) {
            viewModelIllustrator.createPager(idIllustrator)

            lifecycleScope.launch {
                viewModelIllustrator.pagerIllustrator?.collectLatest { pagingData ->
                    adapterIllustrator.submitData(pagingData)
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