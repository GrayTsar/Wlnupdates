package com.graytsar.wlnupdates.ui.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentGroupFeedBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentGroupFeed(private val viewModelGroup: ViewModelGroup, private val idGroup: Int) : Fragment() {
    private lateinit var binding: FragmentGroupFeedBinding
    private val adapterGroupFeed = PagingAdapterGroupFeed(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupFeedBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbarCollectionGroup)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerGroupFeed.adapter = adapterGroupFeed

        viewModelGroup.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarGroupFeed.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        viewModelGroup.errorResponseGroup.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelGroup.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        lifecycleScope.launch {
            adapterGroupFeed.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelGroup.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelGroup.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelGroup.setLoadingIndicator(false)
                    }
                    else -> {
                        viewModelGroup.setLoadingIndicator(false)
                    }
                }
            }
        }

        viewModelGroup.pagerGroupFeed.observe(viewLifecycleOwner) { flow ->
            lifecycleScope.launch {
                flow.collectLatest { pagingData ->
                    adapterGroupFeed.submitData(pagingData)
                }
            }
        }

        return binding.root
    }

    private fun showErrorDialog(title:String, message:String?){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.alert_dialog_ok), null)
            .show()
    }
}