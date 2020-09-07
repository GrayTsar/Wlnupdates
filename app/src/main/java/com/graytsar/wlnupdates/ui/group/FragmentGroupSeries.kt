package com.graytsar.wlnupdates.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentGroupSeriesBinding

class FragmentGroupSeries(private val viewModelGroup: ViewModelGroup, private val idGroup: Int) : Fragment() {
    private lateinit var binding: FragmentGroupSeriesBinding
    private val adapterGroupSeries = AdapterGroupSeries(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupSeriesBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbarCollectionGroup)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.recyclerGroupSeries.adapter = adapterGroupSeries
        binding.recyclerGroupSeries.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as LinearLayoutManager).let { layoutManager ->
                    val totalItemCount = layoutManager.itemCount
                    val visibleItemCount = layoutManager.childCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if(firstVisibleItem + visibleItemCount >= totalItemCount) {
                        viewModelGroup.getMoreActiveSeries()
                    }
                }
            }
        })

        viewModelGroup.name.observe(viewLifecycleOwner) {
            toolbar.title = it
        }

        viewModelGroup.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarGroupSeries.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        viewModelGroup.progressLoading.observe(viewLifecycleOwner) {
            binding.progressBarGroupSeries.progress = it
        }

        viewModelGroup.activeSeries.observe(viewLifecycleOwner, {
            val mapList = ArrayList<Map.Entry<String, String>>()
            it.forEach { entry ->
                mapList.add(entry)
            }
            adapterGroupSeries.submitList(mapList.toMutableList())
        })

        viewModelGroup.errorResponseGroup.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelGroup.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelGroup.errorServerGroup.observe(viewLifecycleOwner) {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(idGroup > 0) {
            viewModelGroup.getDataGroup(idGroup)
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