package com.graytsar.wlnupdates.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestLogin
import com.graytsar.wlnupdates.rest.request.RequestWatchList
import com.graytsar.wlnupdates.rest.response.ResponseLogin
import com.graytsar.wlnupdates.rest.response.ResponseWatchList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelLogin: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)



    private var requestCall: Call<ResponseLogin>? = null

    val responseLogin = MutableLiveData<ResponseLogin>()
    val errorResponseLogin = MutableLiveData<ResponseLogin>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerLogin = MutableLiveData<Response<ResponseLogin>>()

    fun getLogin(username: String, password: String) {
        requestCall?.cancel()
        requestCall = RestService.restService.getLogin(RequestLogin(username, password))
        isLoading.postValue(true)
        requestCall?.enqueue(object: Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if(response.isSuccessful){
                    response.body()?.let { responseLogin ->
                        if(!responseLogin.error) {
                            onReceivedResult(responseLogin)
                        } else {
                            errorResponseLogin.postValue(responseLogin)
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseLogin.postValue(it)
                    } ?: let {
                        errorServerLogin.postValue(response)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                isLoading.postValue(false)
                //Log.d("DBG-Failure:", "restService.getIllustrator() onFailure")
            }
        })
    }

    fun getTest() {
        val requestCall = RestService.restService.getWatchList(RequestWatchList())
        requestCall.enqueue(object: Callback<ResponseWatchList> {
            override fun onResponse(call: Call<ResponseWatchList>, response: Response<ResponseWatchList>) {
                if(response.isSuccessful){
                    response.body()?.let { responseLogin ->
                        if(!responseLogin.error) {

                        }
                    }
                } else {
                    response.body()?.let {

                    }
                }
            }

            override fun onFailure(call: Call<ResponseWatchList>, t: Throwable) {
                val c = call.request()
                if(!call.isCanceled){

                }
            }
        })
    }

    private fun onReceivedResult(result: ResponseLogin){
        responseLogin.postValue(result)
    }
}