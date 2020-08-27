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
import com.graytsar.wlnupdates.databinding.FragmentTranslatedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentTranslated : Fragment() {
    private lateinit var binding: FragmentTranslatedBinding
    private val viewModelTranslated by activityViewModels<ViewModelTranslated>()

    private val adapter = AdapterItem(this)

    private lateinit var recyclerTranslated: RecyclerView

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
        binding = FragmentTranslatedBinding.inflate(inflater, container, false)


        val toolbar: Toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_recent)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)


        recyclerTranslated = binding.recyclerTranslated
        recyclerTranslated.adapter = adapter

        viewModelTranslated.isLoading.observe(viewLifecycleOwner, Observer {
            binding.progressBarTranslated.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        recyclerTranslated.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as LinearLayoutManager).let { layoutManager ->
                    val totalItemCount = layoutManager.itemCount
                    val visibleItemCount = layoutManager.childCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if(firstVisibleItem + visibleItemCount >= totalItemCount) {
                        if(viewModelTranslated.hasNext && !viewModelTranslated.isLoading.value!!){
                            GlobalScope.launch {
                                viewModelTranslated.getTranslatedData(viewModelTranslated.nextNum)
                            }.invokeOnCompletion {
                                lifecycleScope.launch {
                                    adapter.submitList(viewModelTranslated.items.toMutableList())
                                }
                            }
                        }
                    }
                }
            }
        })


        GlobalScope.launch {
            viewModelTranslated.getTranslatedData()
        }.invokeOnCompletion {
            lifecycleScope.launch {
                adapter.submitList(viewModelTranslated.items.toMutableList())
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
                    adapter.submitList(viewModelTranslated.items.toMutableList())
                }
                return false
            }
        })
    }

}