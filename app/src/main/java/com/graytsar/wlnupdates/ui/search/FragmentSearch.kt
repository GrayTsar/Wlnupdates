package com.graytsar.wlnupdates.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.databinding.FragmentSearchBinding
import com.graytsar.wlnupdates.rest.MatchContent
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestSearch
import com.graytsar.wlnupdates.rest.response.ResponseSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentSearch : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private val viewModelSearch by viewModels<ViewModelSearch>()
    private val adapterSearch = AdapterSearch(this)

    private var requestCall:Call<ResponseSearch>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarSearch.toolbarSearch
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        binding.includeToolbarSearch.recyclerSearch.adapter = adapterSearch

        adapterSearch.submitList(viewModelSearch.list.toMutableList())


        val searchBar = binding.includeToolbarSearch.editTextSearchNovel
        searchBar.setText(viewModelSearch.query)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //if(s.length > 1){
                    viewModelSearch.query = s.toString()

                    requestCall?.cancel()
                    requestCall = RestService.restService.getSearch(RequestSearch(viewModelSearch.query))
                    requestCall?.enqueue(object:Callback<ResponseSearch> {
                        override fun onResponse(call: Call<ResponseSearch>, response: Response<ResponseSearch>) {
                            if(response.isSuccessful){
                                response.body()?.error?.let { isError ->
                                    if(isError){
                                        Log.d("DBG-Error:", "${response.body()?.message}")
                                    } else {
                                        onReceivedResult(response.body())
                                    }

                                }
                            } else {
                                Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                            Log.d("DBG-Failure:", "restService.getSearch() onFailure")
                        }
                    })
                }
            //}
        })

        return binding.root
    }

    private fun onReceivedResult(result: ResponseSearch?){
        result?.let { response ->
            viewModelSearch.list.clear()

            response.dataSearch?.results?.forEach { search ->
                search.match?.forEach { item ->
                    viewModelSearch.list.add(MatchContent(search.sid!!, item[0] as Double, item[1] as String))
                }
            }

            adapterSearch.submitList(viewModelSearch.list.toMutableList())
        }
        Log.d("DBG-Failure:", "End")
    }
}