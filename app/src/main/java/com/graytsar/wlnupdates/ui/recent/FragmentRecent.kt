package com.graytsar.wlnupdates.ui.recent

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentRecentBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentRecent : Fragment() {
    private lateinit var binding: FragmentRecentBinding
    private val viewModelRecent by activityViewModels<ViewModelRecent>()

    private val adapter = AdapterItem(this)

    private lateinit var recyclerRecent:RecyclerView

    private var searchView:SearchView? = null


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

        viewModelRecent.isLoading.observe(viewLifecycleOwner, Observer {
            binding.progressBarRecent.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        recyclerRecent.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as LinearLayoutManager).let { layoutManager ->
                    val totalItemCount = layoutManager.itemCount
                    val visibleItemCount = layoutManager.childCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if(firstVisibleItem + visibleItemCount >= totalItemCount) {
                        if(viewModelRecent.hasNext && !viewModelRecent.isLoading.value!!){
                            GlobalScope.launch {
                                viewModelRecent.getRecentData(viewModelRecent.nextNum)
                            }.invokeOnCompletion {
                                lifecycleScope.launch {
                                    adapter.submitList(viewModelRecent.items.toMutableList())
                                }
                            }
                        }
                    }
                }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_recent,menu)

        searchView = menu.findItem(R.id.menuSearchRecent).actionView as SearchView

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    adapter.pattern = it
                    adapter.submitList(viewModelRecent.items.toMutableList())
                }
                return false
            }
        })
    }

}