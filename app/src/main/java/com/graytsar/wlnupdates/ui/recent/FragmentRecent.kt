package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentRecentBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentRecent : Fragment() {
    private lateinit var binding: FragmentRecentBinding
    private val viewModelRecent by activityViewModels<ViewModelRecent>()

    private val adapter = AdapterItem(this)

    private lateinit var recyclerRecent:RecyclerView

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
        // Inflate the layout for this fragment
        binding = FragmentRecentBinding.inflate(inflater, container, false)

        recyclerRecent = binding.recyclerRecent
        recyclerRecent.adapter = adapter

        nestedScrollView = requireActivity().findViewById(R.id.nestedScrollRecent) as NestedScrollView

        viewModelRecent.isLoading.observe(viewLifecycleOwner, Observer {
            binding.progressBarRecent.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        GlobalScope.launch {
            viewModelRecent.getRecentData()
        }.invokeOnCompletion {
            lifecycleScope.launch {
                adapter.submitList(viewModelRecent.items.toMutableList())
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val layoutManager = recyclerRecent.layoutManager as LinearLayoutManager
        val totalItemCount = layoutManager.itemCount
        val visibleItemCount = layoutManager.childCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()


        Log.d("DBG", "Resume Recent $totalItemCount $visibleItemCount $lastVisibleItem")

        nestedScrollView?.setOnScrollChangeListener(scrollListener)
    }

    override fun onPause() {
        super.onPause()
        Log.d("DBG", "Pause Recent")

        val m:NestedScrollView.OnScrollChangeListener? = null
        nestedScrollView?.setOnScrollChangeListener(m)
    }

    private val scrollListener = NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int , scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
        Log.d("DBG", "Scroll Recent")

        if(v.getChildAt(v.childCount - 1) != null) {

            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) && scrollY > oldScrollY) {
                if(viewModelRecent.hasNext && !viewModelRecent.isLoading.value!!){
                    GlobalScope.launch {
                        viewModelRecent.getRecentData(viewModelRecent.nextNum)
                    }.invokeOnCompletion {
                        lifecycleScope.launch {
                            adapter.submitList(viewModelRecent.items.toMutableList())
                        }
                    }
                }
                //code to fetch more data for endless scrolling
            }
        }
    }
}