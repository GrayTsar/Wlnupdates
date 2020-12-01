package com.graytsar.wlnupdates.ui.search

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
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentSearchBinding
import com.graytsar.wlnupdates.extensions.FunctionExtensions.getQueryTextChangeStateFlow
import com.graytsar.wlnupdates.rest.response.ResponseSearch
import com.graytsar.wlnupdates.utils.ErrorSearchListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import retrofit2.Response


class FragmentSearch : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private val viewModelSearch by viewModels<ViewModelSearch>()
    private val adapterSearch = PagingAdapterSearch(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_collection_search)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerSearch.adapter = adapterSearch


        val searchBar = binding.editTextSearchNovel
        //searchBar.setText(viewModelSearch.searchText)


        lifecycleScope.launch {
            searchBar.getQueryTextChangeStateFlow()
                .debounce(600)
                .filter {
                    return@filter !(it.isNullOrEmpty() || it.length < 2)
                }
                .collect {
                    viewModelSearch.doSearch(it)
                }
        }

        viewModelSearch.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarSearch.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })



        viewModelSearch.setErrorSearchListener(object: ErrorSearchListener {
            override fun onSubmitErrorResponse(response: ResponseSearch) {
                response.message?.let { message ->
                    if(message.contains("rate limited")) {

                    } else {
                        showErrorDialog(getString(R.string.alert_dialog_title_error), response.message)
                    }
                }
            }

            override fun onSubmitFailure(throwable: Throwable) {
                showErrorDialog(getString(R.string.alert_dialog_title_failure), throwable.message)
            }

            override fun onSubmitErrorServer(response: Response<ResponseSearch>?) {
                showErrorDialog(getString(R.string.alert_dialog_title_error), response?.code().toString())
            }

        })

        lifecycleScope.launch {
            adapterSearch.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelSearch.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelSearch.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelSearch.setLoadingIndicator(false)
                    }
                    else -> {
                        viewModelSearch.setLoadingIndicator(false)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModelSearch.pagerSearch.collectLatest { pagingData ->
                adapterSearch.submitData(pagingData)
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        viewModelSearch.errorListener = null

        super.onDestroyView()
    }

    private fun showErrorDialog(title:String, message:String?){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.alert_dialog_ok), null)
            .show()
    }
}