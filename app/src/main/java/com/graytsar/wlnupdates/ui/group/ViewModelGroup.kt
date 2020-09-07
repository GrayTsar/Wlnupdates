package com.graytsar.wlnupdates.ui.group

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.internal.LinkedTreeMap
import com.graytsar.wlnupdates.rest.FeedPaginated
import com.graytsar.wlnupdates.rest.ReleasesPaginated
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestGroup
import com.graytsar.wlnupdates.rest.response.ResponseGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelGroup: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val progressLoading = MutableLiveData<Int>(0)

    val name = MutableLiveData<String>("")
    val siteLink = MutableLiveData<String>("")

    private val listSize:Int = 50

    var listActiveSeries = LinkedTreeMap<String, String>()

    var activeSeries = MutableLiveData<LinkedTreeMap<String, String>>()
    var alternateNames = MutableLiveData<List<String?>>()
    var feedPaginated = MutableLiveData<List<FeedPaginated?>>()
    var releasesPaginated = MutableLiveData<List<ReleasesPaginated?>>()

    private var requestCall: Call<ResponseGroup>? = null

    val errorResponseGroup = MutableLiveData<ResponseGroup>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerGroup = MutableLiveData<Response<ResponseGroup>>()

    fun getDataGroup(id:Int) {
        requestCall?.cancel()
        requestCall = RestService.restService.getGroup(RequestGroup(id))
        setLoadingIndicator(true, 25)
        requestCall?.enqueue(object: Callback<ResponseGroup> {
            override fun onResponse(call: Call<ResponseGroup>, response: Response<ResponseGroup>) {
                if(response.isSuccessful){
                    response.body()?.let { responseGroup ->
                        if(!responseGroup.error!!) {
                            onReceivedResult(responseGroup)
                        } else {
                            errorResponseGroup.postValue(responseGroup)
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseGroup.postValue(it)
                    } ?: let {
                        errorServerGroup.postValue(response)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                setLoadingIndicator(false, 100)
            }

            override fun onFailure(call: Call<ResponseGroup>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                setLoadingIndicator(false, 100)
                //Log.d("DBG-Failure:", "restService.getGroup() onFailure")
            }
        })
    }

    private fun onReceivedResult(result: ResponseGroup){
        result.data?.let { dataGroup ->
            name.postValue(dataGroup.group)

            dataGroup.feedPaginated?.let {
                //listFeedPaginated = ArrayList(it)
                feedPaginated.postValue(it)
            }

            dataGroup.activeSeries?.let {
                listActiveSeries = it

                val c = LinkedTreeMap<String, String>()
                var i:Int = 0

                for(entry in listActiveSeries) {
                    i += 1
                    c[entry.key] = entry.value

                    if(i == listSize) {
                        break
                    }
                }

                activeSeries.postValue(c)
            }

            //activeSeries.postValue(dataGroup.activeSeries)
            //feedPaginated.postValue(dataGroup.feedPaginated)
        }
    }

    fun getMoreActiveSeries(){
        activeSeries.value?.let { listSeries ->
            if(listSeries.size >= listActiveSeries.size){
                return
            }

            val tempMap = LinkedTreeMap<String, String>()
            var i:Int = 0
            val currentSize = listSeries.size
            val requestedSize = currentSize + listSize

            for(entry in listActiveSeries) {
                i += 1
                tempMap[entry.key] = entry.value

                if(i == requestedSize) {
                    break
                }
            }
            activeSeries.postValue(tempMap)
        }
    }

    private fun setLoadingIndicator(isVisible: Boolean, progress:Int){
        progressLoading.postValue(progress)
        isLoading.postValue(isVisible)
    }
}