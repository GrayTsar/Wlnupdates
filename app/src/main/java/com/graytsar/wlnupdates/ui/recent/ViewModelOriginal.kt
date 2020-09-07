package com.graytsar.wlnupdates.ui.recent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestOriginal
import com.graytsar.wlnupdates.rest.response.ResponseOriginal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelOriginal: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val progressLoading = MutableLiveData<Int>(0)

    var currentPage:Int = 0

    var hasNext: Boolean = false
    var hasPrev: Boolean = false

    var nextNum: Int = 0
    var prevNum: Int = 0

    private val items = ArrayList<Item>()
    val list = MutableLiveData<List<Item>>()

    private var requestCall: Call<ResponseOriginal>? = null

    val errorResponseOriginal = MutableLiveData<ResponseOriginal>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerOriginal = MutableLiveData<Response<ResponseOriginal>>()

    fun getOriginalsData(offset:Int = 1) {
        if(currentPage == offset) {
            return
        }

        setLoadingIndicator(true, 25)
        requestCall?.cancel()
        requestCall = RestService.restService.getOriginal(RequestOriginal(offset))
        requestCall?.enqueue(object: Callback<ResponseOriginal> {
            override fun onResponse(call: Call<ResponseOriginal>, response: Response<ResponseOriginal>) {
                if(response.isSuccessful){
                    response.body()?.let { responseOriginal ->
                        if(responseOriginal.error!!){
                            errorResponseOriginal.postValue(responseOriginal)
                            //Log.d("DBG-Error:", "${response.body()?.message}")
                        } else {
                            onReceivedResult(response.body(), offset)
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseOriginal.postValue(it)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                } ?: let {
                    errorServerOriginal.postValue(response)
                }
                setLoadingIndicator(false, 100)
            }

            override fun onFailure(call: Call<ResponseOriginal>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                setLoadingIndicator(false, 100)
                //Log.d("DBG-Failure:", "restService.getOriginal() onFailure    ${call.isCanceled}")
            }
        })
    }

    private fun onReceivedResult(result: ResponseOriginal?, offset: Int){
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