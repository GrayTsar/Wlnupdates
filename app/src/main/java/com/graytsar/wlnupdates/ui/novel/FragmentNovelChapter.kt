package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.ARG_PARCEL_NOVEL_CHAPTER
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentNovelChapterBinding
import com.graytsar.wlnupdates.rest.Release
import java.util.*
import kotlin.collections.ArrayList


class FragmentNovelChapter : Fragment() {
    private lateinit var binding: FragmentNovelChapterBinding
    private val adapter = AdapterNovelChapter(this)
    var list: ArrayList<Release>? = null

    private var searchView:SearchView? = null
    private var pattern = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelableArrayList<Release>(ARG_PARCEL_NOVEL_CHAPTER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentNovelChapterBinding.inflate(inflater, container, false)


        val toolbar: Toolbar = binding.includeToolbarSimpleChapter.toolbarSimple
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)


        binding.recyclerNovelChapter.adapter = adapter

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
                val filteredList = ArrayList<Release>()

                newText?.let { newText ->
                    pattern = newText.toLowerCase(Locale.getDefault()).trim()

                    if(pattern.isNotEmpty()) {
                        list?.forEach { release ->
                            var name:String = ""
                            var postfix:String = ""
                            var tlGroup:String = ""
                            var volume:String = ""
                            var chapter:String = ""

                            release.series?.name?.let {
                                name = it.toLowerCase(defaultLocale)
                            }
                            release.postfix?.let {
                                postfix = it.toLowerCase(defaultLocale)
                            }
                            release.tlgroup?.name?.let {
                                tlGroup = it.toLowerCase(defaultLocale)
                            }
                            release.volume?.let {
                                volume = it.toString()
                            }
                            release.chapter?.let {
                                chapter = it.toString()
                            }

                            if( name.contains(pattern) || postfix.contains(pattern) || tlGroup.contains(pattern) || volume.contains(pattern) || chapter.contains(pattern)) {
                                filteredList.add(release)
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