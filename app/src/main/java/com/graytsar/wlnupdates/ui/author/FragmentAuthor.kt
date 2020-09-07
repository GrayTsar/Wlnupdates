package com.graytsar.wlnupdates.ui.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.ARG_ID_AUTHOR
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentAuthorBinding

class FragmentAuthor : Fragment() {
    private lateinit var binding:FragmentAuthorBinding
    private val viewModelAuthor by viewModels<ViewModelAuthor>()
    private val adapterAuthor = AdapterAuthor(this)

    private var idAuthor:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idAuthor = it.getInt(ARG_ID_AUTHOR, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModelAuthor = viewModelAuthor

        val toolbar: Toolbar = binding.includeToolbarAuthor.toolbarAuthor
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerAuthor.adapter = adapterAuthor

        viewModelAuthor.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarAuthor.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        viewModelAuthor.progressLoading.observe(viewLifecycleOwner) {
            binding.progressBarAuthor.progress = it
        }

        viewModelAuthor.name.observe(viewLifecycleOwner) {
            toolbar.title = it
        }

        viewModelAuthor.list.observe(viewLifecycleOwner, {
            adapterAuthor.submitList(it)
        })

        viewModelAuthor.errorResponseAuthor.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelAuthor.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelAuthor.errorServerAuthor.observe(viewLifecycleOwner) {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(idAuthor > 0) {
            viewModelAuthor.getDataAuthor(idAuthor)
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