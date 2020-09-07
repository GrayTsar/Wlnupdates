package com.graytsar.wlnupdates.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentSearchBinding


class FragmentSearch : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private val viewModelSearch by viewModels<ViewModelSearch>()
    private val adapterSearch = AdapterSearch(this)

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

        adapterSearch.submitList(viewModelSearch.list.toMutableList())

        val searchBar = binding.editTextSearchNovel
        searchBar.setText(viewModelSearch.searchText)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.length > 1){
                    viewModelSearch.searchText = s.toString()
                    viewModelSearch.getSearchQuery(s.toString())
                }
            }
        })

        viewModelSearch.listMatchContent.observe(viewLifecycleOwner, {
            adapterSearch.submitList(it.toMutableList())
        })

        viewModelSearch.errorResponseSearch.observe(viewLifecycleOwner, {
            it.message?.let { message ->
                if(message.contains("rate limited")) {

                } else {
                    showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
                }
            }
        })

        viewModelSearch.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelSearch.errorServerSearch.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        })

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