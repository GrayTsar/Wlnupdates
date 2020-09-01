package com.graytsar.wlnupdates.ui.author

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.SeriesTitle
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestAuthor
import com.graytsar.wlnupdates.rest.response.ResponseAuthor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelAuthor: ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val name = MutableLiveData<String>("")
    var list = MutableLiveData<List<SeriesTitle?>>()

    private var requestCall: Call<ResponseAuthor>? = null

    val errorResponseAuthor = MutableLiveData<ResponseAuthor>()
    val failureResponse = MutableLiveData<Throwable>()

    fun getDataAuthor(id:Int) {
        requestCall?.cancel()
        requestCall = RestService.restService.getAuthor(RequestAuthor(id))
        isLoading.postValue(true)
        requestCall?.enqueue(object: Callback<ResponseAuthor> {
            override fun onResponse(call: Call<ResponseAuthor>, response: Response<ResponseAuthor>) {
                if(response.isSuccessful){
                    response.body()?.let { responseAuthor ->
                        if(!responseAuthor.error!!) {
                            onReceivedResult(responseAuthor)
                        } else {
                            errorResponseAuthor.postValue(responseAuthor)
                            Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseAuthor.postValue(it)
                    }
                    Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseAuthor>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                isLoading.postValue(false)
                Log.d("DBG-Failure:", "restService.getAuthor() onFailure")
            }
        })
    }

    private fun onReceivedResult(result: ResponseAuthor){
        result.data?.let {
            name.postValue(it.name)
            list.postValue(it.series)

        }
    }
}