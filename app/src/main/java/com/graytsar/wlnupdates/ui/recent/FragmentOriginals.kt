package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentOriginalsBinding
import com.graytsar.wlnupdates.databinding.FragmentRecentBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentOriginals : Fragment() {
    private lateinit var binding: FragmentOriginalsBinding
    private val viewModelOriginal by activityViewModels<ViewModelOriginal>()

    private val adapter = AdapterItem(this)

    private lateinit var recyclerOriginal: RecyclerView

    private var nestedScrollView:NestedScrollView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOriginalsBinding.inflate(inflater, container, false)

        recyclerOriginal = binding.recyclerOriginal
        recyclerOriginal.adapter = adapter

        nestedScrollView = requireActivity().findViewById(R.id.nestedScrollRecent) as NestedScrollView

        viewModelOriginal.isLoading.observe(viewLifecycleOwner, Observer {
            binding.progressBarOriginal.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })


        GlobalScope.launch {
            viewModelOriginal.getOriginalsData()
        }.invokeOnCompletion {
            lifecycleScope.launch {
                adapter.submitList(viewModelOriginal.items.toMutableList())
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val layoutManager = recyclerOriginal.layoutManager as LinearLayoutManager
        val totalItemCount = layoutManager.itemCount
        val visibleItemCount = layoutManager.childCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        Log.d("DBG", "Resume Original $totalItemCount $visibleItemCount $lastVisibleItem")
        nestedScrollView?.setOnScrollChangeListener(scrollListener)
    }

    override fun onPause() {
        super.onPause()
        Log.d("DBG", "Pause Original")

        val m:NestedScrollView.OnScrollChangeListener? = null
        nestedScrollView?.setOnScrollChangeListener(m)
    }

    private val scrollListener = NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int , scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
        Log.d("DBG", "Scroll Original")

        if(v.getChildAt(v.childCount - 1) != null) {

            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) && scrollY > oldScrollY) {
                if(viewModelOriginal.hasNext && !viewModelOriginal.isLoading.value!!){
                    GlobalScope.launch {
                        viewModelOriginal.getOriginalsData(viewModelOriginal.nextNum)
                    }.invokeOnCompletion {
                        lifecycleScope.launch {
                            adapter.submitList(viewModelOriginal.items.toMutableList())
                        }
                    }
                }
                //code to fetch more data for endless scrolling
            }
        }
    }
}