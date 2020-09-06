package com.graytsar.wlnupdates.ui.recent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestTranslated
import com.graytsar.wlnupdates.rest.response.ResponseTranslated
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelTranslated: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    var currentPage:Int = 0

    var hasNext: Boolean = false
    var hasPrev: Boolean = false

    var nextNum: Int = 0
    var prevNum: Int = 0

    private val items = ArrayList<Item>()
    val list = MutableLiveData<List<Item>>()

    private var requestCall: Call<ResponseTranslated>? = null

    val errorResponseTranslated = MutableLiveData<ResponseTranslated>()
    val failureResponse = MutableLiveData<Throwable>()

    fun getTranslatedData(offset:Int = 1) {
        if(currentPage == offset) {
            return
        }

        isLoading.postValue(true)
        requestCall?.cancel()
        requestCall = RestService.restService.getTranslated(RequestTranslated(offset))
        requestCall?.enqueue(object: Callback<ResponseTranslated> {
            override fun onResponse(call: Call<ResponseTranslated>, response: Response<ResponseTranslated>) {
                if(response.isSuccessful){
                    response.body()?.let { responseTranslated ->
                        if(responseTranslated.error!!){
                            errorResponseTranslated.postValue(responseTranslated)
                            //Log.d("DBG-Error:", "${response.body()?.message}")
                        } else {
                            onReceivedResult(response.body(), offset)
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseTranslated.postValue(it)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<ResponseTranslated>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                //Log.d("DBG-Failure:", "restService.getTranslated() onFailure    ${call.isCanceled}")
            }
        })
    }

    private fun onReceivedResult(result: ResponseTranslated?, offset: Int){
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
            isLoading.postValue(false)
        }
    }
}