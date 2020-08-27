package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_PARCEL_NOVEL_TAG
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentNovelTagBinding
import com.graytsar.wlnupdates.rest.Tag
import java.util.*
import kotlin.collections.ArrayList

class FragmentNovelTag : Fragment() {
    private lateinit var binding: FragmentNovelTagBinding
    private val adapter = AdapterNovelTag(this)
    var list: ArrayList<Tag>? = null

    private var searchView:SearchView? = null
    private var pattern = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelableArrayList<Tag>(ARG_PARCEL_NOVEL_TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentNovelTagBinding.inflate(inflater, container, false)


        val toolbar: Toolbar = binding.includeToolbarSimpleTag.toolbarSimple
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)


        binding.recyclerNovelTag.adapter = adapter

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
                val filteredList = ArrayList<Tag>()

                newText?.let {
                    pattern = it.toLowerCase(defaultLocale).trim()

                    if(pattern.isNotEmpty()) {
                        list?.forEach { tag ->
                            tag.tag?.let { str ->
                                if(str.toLowerCase(defaultLocale).contains(pattern)){
                                    filteredList.add(tag)
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