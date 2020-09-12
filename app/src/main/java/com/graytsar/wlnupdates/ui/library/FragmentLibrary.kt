package com.graytsar.wlnupdates.ui.library

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.flexbox.*
import com.google.android.play.core.review.ReviewManagerFactory
import com.graytsar.wlnupdates.*
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.ModelLibrary
import com.graytsar.wlnupdates.databinding.FragmentLibraryBinding
import kotlinx.coroutines.launch
import java.lang.Exception

class FragmentLibrary : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val viewModelLibrary by viewModels<ViewModelLibrary>()
    private val adapter = AdapterItemLibrary(this)

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
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarLibrary.toolbarLibrary
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        val flexBoxLayoutManager = FlexboxLayoutManager(context)
        flexBoxLayoutManager.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            alignItems = AlignItems.BASELINE
            flexWrap = FlexWrap.WRAP
        }

        binding.recyclerLibrary.layoutManager = flexBoxLayoutManager
        binding.recyclerLibrary.adapter = adapter

        var array:LiveData<List<ModelLibrary>>? = null
        lifecycleScope.launch {
            array = DatabaseService.db?.daoLibrary()!!.selectAll()
        }.invokeOnCompletion {
            array?.observe(viewLifecycleOwner) { list ->
                val sortedList = list.sortedBy {
                    it.position
                }

                var i:Long = 1
                sortedList.forEach { model ->
                    model.position = i
                    i++
                }


                adapter.submitList(sortedList)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestReview()
    }

    override fun onStop() {
        super.onStop()
        adapter.currentList.forEach {
            DatabaseService.db?.daoLibrary()!!.update(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_library,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        when(item.itemId) {
            R.id.menuLogin -> {
                navController.navigate(R.id.fragmentLogin)
            }
            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun requestReview(){
        try {
            val sharedPref = activity?.getSharedPreferences(keyPreferenceUtils, Context.MODE_PRIVATE)
            sharedPref?.let { sharedPreferences ->
                var count = sharedPreferences.getInt(keyPreferenceRequestReview, 0)
                count += 1

                if(count == requestReviewAt) {

                    val manager = ReviewManagerFactory.create(requireContext())
                    val request = manager.requestReviewFlow()
                    request.addOnCompleteListener { req ->
                        if(req.isSuccessful){
                            context?.let { context ->
                                if(context is MainActivity) {
                                    val reviewInfo = req.result
                                    val flow = manager.launchReviewFlow(context, reviewInfo)
                                    flow.addOnCompleteListener {
                                        // The flow has finished. The API does not indicate whether the user
                                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                                        // matter the result, we continue our app flow.
                                    }
                                }
                            }
                        } else {
                            // There was some problem, continue regardless of the result.
                        }
                    }
                }

                val editor = sharedPreferences.edit()
                editor.putInt(keyPreferenceRequestReview, count.rem(requestReviewAt))
                editor.apply()
            }
        } catch (e: Exception) {

        }
    }
}