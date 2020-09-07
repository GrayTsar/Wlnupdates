package com.graytsar.wlnupdates.ui.publisher

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.SeriesTitle
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestPublisher
import com.graytsar.wlnupdates.rest.response.ResponsePublisher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelPublisher: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val progressLoading = MutableLiveData<Int>(0)

    val name = MutableLiveData<String>("")
    var list = MutableLiveData<List<SeriesTitle?>>()

    private var requestCall: Call<ResponsePublisher>? = null

    val errorResponsePublisher = MutableLiveData<ResponsePublisher>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerPublisher = MutableLiveData<Response<ResponsePublisher>>()

    fun getDataPublisher(id:Int) {
        requestCall?.cancel()
        requestCall = RestService.restService.getPublisher(RequestPublisher(id))
        setLoadingIndicator(true, 25)
        requestCall?.enqueue(object: Callback<ResponsePublisher> {
            override fun onResponse(call: Call<ResponsePublisher>, response: Response<ResponsePublisher>) {
                if(response.isSuccessful){
                    response.body()?.let { responsePublisher ->
                        if(!responsePublisher.error!!) {
                            onReceivedResult(responsePublisher)
                        } else {
                            errorResponsePublisher.postValue(responsePublisher)
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponsePublisher.postValue(it)
                    } ?: let {
                        errorServerPublisher.postValue(response)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                setLoadingIndicator(false, 100)
            }

            override fun onFailure(call: Call<ResponsePublisher>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                setLoadingIndicator(false, 100)
                //Log.d("DBG-Failure:", "restService.getPublisher() onFailure")
            }
        })
    }

    private fun onReceivedResult(result: ResponsePublisher){
        result.data?.let {
            name.postValue(it.name)
            list.postValue(it.series)

        }
    }

    private fun setLoadingIndicator(isVisible: Boolean, progress:Int){
        progressLoading.postValue(progress)
        isLoading.postValue(isVisible)
    }
}