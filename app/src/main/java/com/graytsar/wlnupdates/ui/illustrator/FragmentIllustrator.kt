package com.graytsar.wlnupdates.ui.illustrator

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
import com.graytsar.wlnupdates.ARG_ID_ILLUSTRATOR
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentIllustratorBinding

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

        viewModelIllustrator.progressLoading.observe(viewLifecycleOwner) {
            binding.progressBarIllustrator.progress = it
        }

        viewModelIllustrator.name.observe(viewLifecycleOwner) {
            toolbar.title = it
        }

        viewModelIllustrator.list.observe(viewLifecycleOwner, {
            adapterIllustrator.submitList(it)
        })

        viewModelIllustrator.errorResponseIllustrator.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelIllustrator.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelIllustrator.errorServerIllustrator.observe(viewLifecycleOwner) {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(idIllustrator > 0) {
            viewModelIllustrator.getDataIllustrator(idIllustrator)
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