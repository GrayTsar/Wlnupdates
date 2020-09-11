package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentRecentBinding
import com.graytsar.wlnupdates.extensions.FunctionExtensions.getQueryTextChangeStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentRecent : Fragment() {
    private lateinit var binding: FragmentRecentBinding
    private val viewModelRecent by viewModels<ViewModelRecent>()

    private val adapter = PagingAdapterItem(this)

    private lateinit var recyclerRecent:RecyclerView

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
        binding = FragmentRecentBinding.inflate(inflater, container, false)


        val toolbar: Toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_recent)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)


        recyclerRecent = binding.recyclerRecent
        recyclerRecent.adapter = adapter

        viewModelRecent.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarRecent.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })


        viewModelRecent.errorResponseRecent.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelRecent.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelRecent.errorServerRecent.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        })

        lifecycleScope.launch {
            viewModelRecent.pager.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelRecent.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelRecent.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelRecent.setLoadingIndicator(false)
                    } else -> {
                        viewModelRecent.setLoadingIndicator(false)
                    }
                }
            }
        }

        queryChangedData.observe(viewLifecycleOwner) { query ->
            lifecycleScope.launch {
                if(query.isEmpty()) {
                    viewModelRecent.pager.collect { pagingData ->
                        adapter.submitData(pagingData)
                    }
                } else {
                    viewModelRecent.pager.collect { pagingData ->
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