package com.graytsar.wlnupdates.ui.recent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestRecent
import com.graytsar.wlnupdates.rest.response.ResponseRecent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelRecent: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val progressLoading = MutableLiveData<Int>(0)

    var currentPage:Int = 0

    var hasNext: Boolean = false
    var hasPrev: Boolean = false

    var nextNum: Int = 0
    var prevNum: Int = 0

    private val items = ArrayList<Item>()
    var list = MutableLiveData<List<Item>>()

    private var requestCall: Call<ResponseRecent>? = null

    val errorResponseRecent = MutableLiveData<ResponseRecent>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerRecent = MutableLiveData<Response<ResponseRecent>>()

    fun getRecentData(offset:Int = 1) {
        if(currentPage == offset) {
            return
        }

        setLoadingIndicator(true, 25)
        requestCall?.cancel()
        requestCall = RestService.restService.getRecent(RequestRecent(offset))
        requestCall?.enqueue(object: Callback<ResponseRecent> {
            override fun onResponse(call: Call<ResponseRecent>, response: Response<ResponseRecent>) {
                if(response.isSuccessful){
                    response.body()?.let { responseRecent ->
                        if(responseRecent.error!!){
                            errorResponseRecent.postValue(responseRecent)
                            //Log.d("DBG-Error:", "${response.body()?.message}")
                        } else {
                            onReceivedResult(response.body(), offset)
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseRecent.postValue(it)
                    } ?: let {
                        errorServerRecent.postValue(response)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }
                setLoadingIndicator(false, 100)
            }

            override fun onFailure(call: Call<ResponseRecent>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                setLoadingIndicator(false, 100)
                //Log.d("DBG-Failure:", "restService.getRecent() onFailure    ${call.isCanceled}")
            }
        })
    }

    private fun onReceivedResult(result: ResponseRecent?, offset: Int){
        result?.data?.let { data ->
            data.hasNext?.let {
                hasNext = it
            }
            data.hasPrev?.let {
                hasPrev = it
            }
            data.nextNum?.let {
                nextNum = it
            }
            data.prevNum?.let {
                prevNum = it
            }
            data.items?.forEach {
                items.add(it)
            }
            list.postValue(items.toMutableList())

            currentPage = offset
            setLoadingIndicator(false, 100)
        }
    }

    private fun setLoadingIndicator(isVisible: Boolean, progress:Int){
        progressLoading.postValue(progress)
        isLoading.postValue(isVisible)
    }
}