package com.graytsar.wlnupdates.ui.illustrator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.SeriesTitle
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestIllustrator
import com.graytsar.wlnupdates.rest.response.ResponseIllustrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelIllustrator: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val name = MutableLiveData<String>("")
    var list = MutableLiveData<List<SeriesTitle?>>()

    private var requestCall: Call<ResponseIllustrator>? = null

    val errorResponseIllustrator = MutableLiveData<ResponseIllustrator>()
    val failureResponse = MutableLiveData<Throwable>()

    fun getDataIllustrator(id:Int) {
        requestCall?.cancel()
        requestCall = RestService.restService.getIllustrator(RequestIllustrator(id))
        isLoading.postValue(true)
        requestCall?.enqueue(object: Callback<ResponseIllustrator> {
            override fun onResponse(call: Call<ResponseIllustrator>, response: Response<ResponseIllustrator>) {
                if(response.isSuccessful){
                    response.body()?.let { responseIllustrator ->
                        if(!responseIllustrator.error!!) {
                            onReceivedResult(responseIllustrator)
                        } else {
                            errorResponseIllustrator.postValue(responseIllustrator)
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseIllustrator.postValue(it)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseIllustrator>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                isLoading.postValue(false)
                //Log.d("DBG-Failure:", "restService.getIllustrator() onFailure")
            }
        })
    }

    private fun onReceivedResult(result: ResponseIllustrator){
        result.data?.let {
            name.postValue(it.name)
            list.postValue(it.series)

        }
    }
}