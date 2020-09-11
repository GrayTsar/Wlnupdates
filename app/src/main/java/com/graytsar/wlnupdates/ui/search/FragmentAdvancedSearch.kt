package com.graytsar.wlnupdates.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.graytsar.wlnupdates.ARG_PARCEL_ADVANCED_SEARCH_RESULT
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.databinding.FragmentAdvancedSearchBinding
import com.graytsar.wlnupdates.rest.ItemGenre
import com.graytsar.wlnupdates.rest.ItemTag
import com.graytsar.wlnupdates.rest.request.RequestAdvancedSearch
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FragmentAdvancedSearch : Fragment() {
    private lateinit var binding: FragmentAdvancedSearchBinding
    private val viewModelAdvancedSearch by viewModels<ViewModelAdvancedSearch>()

    private val adapterGenre = AdapterAdvancedSearchGenre(this)
    private val adapterTag = AdapterAdvancedSearchTag(this)


    private lateinit var recyclerAdvancedGenre:RecyclerView

    private lateinit var btnTypeAll:MaterialButton
    private lateinit var btnTypeOriginal:MaterialButton
    private lateinit var btnTypeTranslated:MaterialButton

    private lateinit var btnSortName:MaterialButton
    private lateinit var btnSortUpdate:MaterialButton
    private lateinit var btnSortChapters:MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvancedSearchBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_collection_search)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        val flexBoxLayoutManagerGenre = FlexboxLayoutManager(context)
        flexBoxLayoutManagerGenre.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
            flexWrap = FlexWrap.WRAP
        }

        recyclerAdvancedGenre = binding.includeItemAdvancedSearchGenre.recyclerAdvancedGenre
        recyclerAdvancedGenre.layoutManager = flexBoxLayoutManagerGenre
        recyclerAdvancedGenre.adapter = ConcatAdapter(adapterGenre, adapterTag)
        recyclerAdvancedGenre.setItemViewCacheSize(200)

        binding.includeItemAdvancedSearchType.lifecycleOwner = this
        binding.includeItemAdvancedSearchType.model = viewModelAdvancedSearch
        btnTypeAll = binding.includeItemAdvancedSearchType.buttonTypeBoth
        btnTypeOriginal = binding.includeItemAdvancedSearchType.buttonTypeOriginal
        btnTypeTranslated = binding.includeItemAdvancedSearchType.buttonTypeTranslated

        binding.includeItemAdvancedSearchSort.lifecycleOwner = this
        binding.includeItemAdvancedSearchSort.model = viewModelAdvancedSearch
        btnSortName = binding.includeItemAdvancedSearchSort.buttonSortName
        btnSortUpdate = binding.includeItemAdvancedSearchSort.buttonSortUpdate
        btnSortChapters = binding.includeItemAdvancedSearchSort.buttonSortCount


        viewModelAdvancedSearch.isLoading.observe(viewLifecycleOwner, {
            binding.progressBarGenre.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        /*
        viewModelAdvancedSearch.listGenre.observe(viewLifecycleOwner, {
            val ar = ArrayList<ItemGenre>()
            ar.add(ItemGenre(0, "Genre", 0))
            ar.addAll(it)
            adapterGenre.submitList(ar)
        })

        viewModelAdvancedSearch.listTag.observe(viewLifecycleOwner, {
            val ar = ArrayList<ItemTag>()
            ar.add(ItemTag(0, "Tag", 0))
            ar.addAll(it)
            adapterTag.submitList(it)
        })

         */


        viewModelAdvancedSearch.typeAll.observe(viewLifecycleOwner, {
            if(it) {
                //viewModelAdvancedSearch.typeAll.value = true
                viewModelAdvancedSearch.typeOriginal.postValue(false)
                viewModelAdvancedSearch.typeTranslated.postValue(false)
            }
        })

        viewModelAdvancedSearch.typeOriginal.observe(viewLifecycleOwner, {
            if(it) {
                //viewModelAdvancedSearch.typeOriginal.value = true
                viewModelAdvancedSearch.typeAll.postValue(false)
                viewModelAdvancedSearch.typeTranslated.postValue(false)
            }
        })

        viewModelAdvancedSearch.typeTranslated.observe(viewLifecycleOwner, {
            if(it) {
                //viewModelAdvancedSearch.typeTranslated.value = true
                viewModelAdvancedSearch.typeAll.postValue(false)
                viewModelAdvancedSearch.typeOriginal.postValue(false)
            }
        })


        viewModelAdvancedSearch.sortName.observe(viewLifecycleOwner, {
            if(it) {
                //viewModelAdvancedSearch.sortName.value = true
                viewModelAdvancedSearch.sortUpdate.postValue(false)
                viewModelAdvancedSearch.sortChapter.postValue(false)
            }
        })

        viewModelAdvancedSearch.sortUpdate.observe(viewLifecycleOwner, {
            if(it) {
                //viewModelAdvancedSearch.sortUpdate.value = true
                viewModelAdvancedSearch.sortName.postValue(false)
                viewModelAdvancedSearch.sortChapter.postValue(false)
            }
        })

        viewModelAdvancedSearch.sortChapter.observe(viewLifecycleOwner, {
            if(it) {
                //viewModelAdvancedSearch.sortChapter.value = true
                viewModelAdvancedSearch.sortName.postValue(false)
                viewModelAdvancedSearch.sortUpdate.postValue(false)
            }
        })

        viewModelAdvancedSearch.listResultSearch.observe(viewLifecycleOwner, {
            it?.let {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_PARCEL_ADVANCED_SEARCH_RESULT, ArrayList(it))

                navController.navigate(R.id.fragmentAdvancedSearchResult, bundle)

                viewModelAdvancedSearch.listResultSearch.value = null
            }
        })

        viewModelAdvancedSearch.errorResponseGenre.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelAdvancedSearch.errorResponseTag.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelAdvancedSearch.errorResponseAdvancedSearch.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.message)
        })

        viewModelAdvancedSearch.failureResponse.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_failure), it.message)
        })

        viewModelAdvancedSearch.errorServerGenre.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        })

        viewModelAdvancedSearch.errorServerTag.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        })

        viewModelAdvancedSearch.errorServerAdvancedSearch.observe(viewLifecycleOwner, {
            showErrorDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        })

        btnTypeAll.setOnClickListener {
            val isActive = viewModelAdvancedSearch.typeAll.value!!

            if(!isActive){
                viewModelAdvancedSearch.typeAll.value = true
            }
        }
        btnTypeOriginal.setOnClickListener {
            val isActive = viewModelAdvancedSearch.typeOriginal.value!!

            if(!isActive){
                viewModelAdvancedSearch.typeOriginal.value = true
            }
        }
        btnTypeTranslated.setOnClickListener {
            val isActive = viewModelAdvancedSearch.typeTranslated.value!!

            if(!isActive){
                viewModelAdvancedSearch.typeTranslated.value = true
            }
        }

        btnSortName.setOnClickListener {
            val isActive = viewModelAdvancedSearch.sortName.value!!

            if(!isActive){
                viewModelAdvancedSearch.sortName.value = true
            }
        }
        btnSortUpdate.setOnClickListener {
            val isActive = viewModelAdvancedSearch.sortUpdate.value!!

            if(!isActive){
                viewModelAdvancedSearch.sortUpdate.value = true
            }
        }
        btnSortChapters.setOnClickListener {
            val isActive = viewModelAdvancedSearch.sortChapter.value!!

            if(!isActive){
                viewModelAdvancedSearch.sortChapter.value = true
            }
        }

        binding.includeItemAdvancedSearchBar.advancedSearchGoButton.setOnClickListener {
            onClickSearch(it)

        }

        lifecycleScope.launch {
            adapterGenre.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelAdvancedSearch.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelAdvancedSearch.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelAdvancedSearch.setLoadingIndicator(false)
                    }
                    else -> {
                        viewModelAdvancedSearch.setLoadingIndicator(false)
                    }
                }
            }
        }

        lifecycleScope.launch {
            adapterTag.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModelAdvancedSearch.setLoadingIndicator(true)
                    }
                    !is LoadState.Loading -> {
                        viewModelAdvancedSearch.setLoadingIndicator(false)
                    }
                    is LoadState.Error -> {
                        viewModelAdvancedSearch.setLoadingIndicator(false)
                    }
                    else -> {
                        viewModelAdvancedSearch.setLoadingIndicator(false)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModelAdvancedSearch.pagerGenre.collectLatest { pagingData ->
                adapterGenre.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            viewModelAdvancedSearch.pagerTag.collectLatest { pagingData ->
                adapterTag.submitData(pagingData)
            }
        }

        return binding.root
    }

    private fun onClickSearch(view: View) {
        val titleSearchText = binding.includeItemAdvancedSearchBar.editTextAdvancedSearchTerm.text.toString()
        var sortMode = ""

        val tAll = viewModelAdvancedSearch.typeAll.value!!
        val tOriginal = viewModelAdvancedSearch.typeOriginal.value!!
        val tTranslated = viewModelAdvancedSearch.typeTranslated.value!!

        val sName = viewModelAdvancedSearch.sortName.value!!
        val sUpdate = viewModelAdvancedSearch.sortUpdate.value!!
        val sChapter = viewModelAdvancedSearch.sortChapter.value!!


        val genreList = ArrayList<ItemGenre>()
        val tagList = ArrayList<ItemTag>()

        val sMap = LinkedHashMap<String, String>() //series type
        val gMap = LinkedHashMap<String, String>() //genre
        val tMap = LinkedHashMap<String, String>() //tag

        adapterGenre.snapshot().forEach{ item ->
            if(item!!.isSelected.value!!) {
                genreList.add(item)
            }
        }

        adapterTag.snapshot().forEach{ item ->
            if(item!!.isSelected.value!!) {
                tagList.add(item)
            }
        }


        if(tAll){

        } else if(tOriginal) {
            sMap["Original English Language"] = "included"
        } else if(tTranslated) {
            sMap["Translated"] = "included"
        }

        if(sName) {
            sortMode = "name"
        } else if(sUpdate) {
            sortMode = "update"
        } else if(sChapter) {
            sortMode = "chapter-count"
        }

        genreList.forEach {
            if(it.isSelected.value!!){
                gMap[it.name] = "include"
            }
        }

        tagList.forEach {
            if(it.isSelected.value!!) {
                tMap[it.name] = "include"
            }
        }

        viewModelAdvancedSearch.getDataAdvancedSearch(RequestAdvancedSearch(titleSearchText, sMap, gMap, tMap, sortMode))
    }

    private fun showErrorDialog(title:String, message:String?){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.alert_dialog_ok), null)
            .show()
    }
}