package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentTranslatedBinding
import com.graytsar.wlnupdates.extensions.FunctionExtensions.getQueryTextChangeStateFlow
import com.graytsar.wlnupdates.rest.response.ResponseTranslated
import com.graytsar.wlnupdates.utils.ErrorTranslatedListener
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class FragmentTranslated : Fragment() {
    private lateinit var binding: FragmentTranslatedBinding
    private val viewModelTranslated by viewModels<ViewModelTranslated>()

    private val adapter = PagingAdapterItem(this)

    private lateinit var recyclerTranslated: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private var searchView:SearchView? = null
    private val queryChangedData = MutableLiveData<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentTranslatedBinding.inflate(inflater, container, false)


        val toolbar: Toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_recent)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)


        swipeRefresh = binding.swipeRefreshTranslated
        swipeRefresh.setOnRefreshListener {
            viewModelTranslated.refresh()
            swipeRefresh.isRefreshing = false
        }

        recyclerTranslated = binding.recyclerTranslated
        recyclerTranslated.adapter = adapter

        viewModelTranslated.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarTranslated.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })


        viewModelTranslated.setErrorTranslatedListener(object: ErrorTranslatedListener{
            override fun onSubmitErrorResponse(response: ResponseTranslated) {
                showErrorDialog(getString(R.string.alert_dialog_title_error), response.message)
            }

            override fun onSubmitFailure(throwable: Throwable) {
                showErrorDialog(getString(R.string.alert_dialog_title_failure), throwable.message)
            }

            override fun onSubmitErrorServer(response: Response<ResponseTranslated>?) {
                showErrorDialog(getString(R.string.alert_dialog_title_error), response?.code().toString())
            }

        })


        lifecycleScope.launch {
            viewModelTranslated.pager.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelTranslated.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelTranslated.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelTranslated.setLoadingIndicator(false)
                    }
                    else -> {
                        viewModelTranslated.setLoadingIndicator(false)
                    }
                }
            }
        }

        queryChangedData.observe(viewLifecycleOwner) { query ->
            lifecycleScope.launch {
                if(query.isEmpty()) {
                    viewModelTranslated.pager.collect { pagingData ->
                        adapter.submitData(pagingData)
                    }
                } else {
                    viewModelTranslated.pager.collect { pagingData ->
                        val filteredData = pagingData.filter { item ->
                            item.series!!.name!!.contains(query, true) || item.tlgroup!!.name!!.contains(query, true)
                        }
                        adapter.submitData(filteredData)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_recent,menu)

        searchView = menu.findItem(R.id.menuSearchRecent).actionView as SearchView

        lifecycleScope.launch {
            searchView!!.getQueryTextChangeStateFlow()
                .debounce(600)
                .collect { query ->
                    queryChangedData.postValue(query)
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