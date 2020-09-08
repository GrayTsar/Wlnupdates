package com.graytsar.wlnupdates.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.MatchContent
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestSearch
import com.graytsar.wlnupdates.rest.response.ResponseSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelSearch: ViewModel() {
    var searchText: String = ""

    val list = ArrayList<MatchContent>()
    val listMatchContent = MutableLiveData<List<MatchContent>>()

    private var requestCall: Call<ResponseSearch>? = null

    val errorResponseSearch = MutableLiveData<ResponseSearch>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerSearch = MutableLiveData<Response<ResponseSearch>>()

    fun getSearchQuery(query: String) {
        requestCall?.cancel()
        requestCall = RestService.restService.getSearch(RequestSearch(query))
        requestCall?.enqueue(object: Callback<ResponseSearch> {
            override fun onResponse(call: Call<ResponseSearch>, response: Response<ResponseSearch>) {
                if(response.isSuccessful){
                    response.body()?.let { responseSearch ->
                        if(responseSearch.error!!){
                            errorResponseSearch.postValue(responseSearch)
                            Log.d("DBG-Error:", "${response.body()?.message}")
                        } else {
                            onReceivedResult(response.body())
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseSearch.postValue(it)
                    } ?: let {
                        errorServerSearch.postValue(response)
                    }
                    Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                Log.d("DBG-Failure:", "restService.getSearch() onFailure    ${call.isCanceled}")
            }
        })
    }

    private fun onReceivedResult(result: ResponseSearch?){
        result?.let { response ->
            list.clear()

            response.dataSearch?.results?.forEach { search ->
                search.match?.forEach { item ->
                    list.add(MatchContent(search.sid!!, item[0] as Double, item[1] as String))
                }
            }

            listMatchContent.postValue(list)
        }
    }
}