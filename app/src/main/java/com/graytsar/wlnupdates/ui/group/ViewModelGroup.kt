package com.graytsar.wlnupdates.ui.group

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.internal.LinkedTreeMap
import com.graytsar.wlnupdates.rest.FeedPaginated
import com.graytsar.wlnupdates.rest.ReleasesPaginated
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestAuthor
import com.graytsar.wlnupdates.rest.request.RequestGroup
import com.graytsar.wlnupdates.rest.response.ResponseAuthor
import com.graytsar.wlnupdates.rest.response.ResponseGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelGroup: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val name = MutableLiveData<String>("")
    val siteLink = MutableLiveData<String>("")


    var activeSeries = MutableLiveData<LinkedTreeMap<String, String>>()
    var alternateNames = MutableLiveData<List<String?>>()
    var feedPaginated = MutableLiveData<List<FeedPaginated?>>()
    var releasesPaginated = MutableLiveData<List<ReleasesPaginated?>>()

    private var requestCall: Call<ResponseGroup>? = null

    fun getDataGroup(id:Int) {
        requestCall?.cancel()
        requestCall = RestService.restService.getGroup(RequestGroup(id))
        isLoading.postValue(true)
        requestCall?.enqueue(object: Callback<ResponseGroup> {
            override fun onResponse(call: Call<ResponseGroup>, response: Response<ResponseGroup>) {
                if(response.isSuccessful){
                    response.body()?.let { responseGroup ->
                        if(!responseGroup.error!!) {
                            onReceivedResult(responseGroup)
                        } else {
                            Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseGroup>, t: Throwable) {
                isLoading.postValue(false)
                Log.d("DBG-Failure:", "restService.getGroup() onFailure")
            }
        })
    }

    private fun onReceivedResult(result: ResponseGroup){
        result.data?.let {
            name.postValue(it.group)
            activeSeries.postValue(it.activeSeries)
            feedPaginated.postValue(it.feedPaginated)

        }
    }











    /*
    fun getDataGroup(id:Int){
        isLoading.postValue(true)

        val result = RestService.restService.getGroup(RequestGroup(id)).execute().body()
        result?.data?.let { data ->

            group.postValue(data.group)

            siteLink.postValue(data.site)

            data.activeSeries?.let {
                activeSeries = it
            }
            data.alternateNames?.let {
                alternateNames = it
            }
            data.feedPaginated?.let {
                feedPaginated = it
            }
            data.releasesPaginated?.let {
                releasesPaginated = it
            }
        }

        isLoading.postValue(false)
    }

     */
}