package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_PARCEL_NOVEL_GENRE
import com.graytsar.wlnupdates.ARG_PARCEL_NOVEL_PUBLISHER
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentNovelIllustratorBinding
import com.graytsar.wlnupdates.databinding.FragmentNovelPublisherBinding
import com.graytsar.wlnupdates.rest.Genre
import com.graytsar.wlnupdates.rest.Publisher
import java.util.*
import kotlin.collections.ArrayList

class FragmentNovelPublisher : Fragment() {
    private lateinit var binding: FragmentNovelPublisherBinding
    private val adapter = AdapterNovelPublisher(this)
    var list: ArrayList<Publisher>? = null

    private var searchView:SearchView? = null
    private var pattern = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelableArrayList<Publisher>(ARG_PARCEL_NOVEL_PUBLISHER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentNovelPublisherBinding.inflate(inflater, container, false)


        val toolbar: Toolbar = binding.includeToolbarSimplePublisher.toolbarSimple
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)


        binding.recyclerNovelPublisher.adapter = adapter

        list?.let {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_novel_detail,menu)

        searchView = menu.findItem(R.id.menuSearchNovel).actionView as SearchView

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val defaultLocale = Locale.getDefault()
                val filteredList = ArrayList<Publisher>()

                newText?.let {
                    pattern = it.toLowerCase(defaultLocale).trim()

                    if(pattern.isNotEmpty()) {
                        list?.forEach { publisher ->
                            publisher.publisher?.let { str ->
                                if(str.toLowerCase(defaultLocale).contains(pattern)){
                                    filteredList.add(publisher)
                                }
                            }
                        }
                        adapter.submitList(filteredList)
                    } else {
                        list?.let { list ->
                            adapter.submitList(list.toMutableList())
                        }
                    }
                }
                return false
            }
        })
    }
}