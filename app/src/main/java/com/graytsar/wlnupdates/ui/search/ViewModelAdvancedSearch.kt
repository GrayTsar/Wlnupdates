package com.graytsar.wlnupdates.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.ItemGenre
import com.graytsar.wlnupdates.rest.ItemTag
import com.graytsar.wlnupdates.rest.data.DataAdvancedSearch
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestAdvancedSearch
import com.graytsar.wlnupdates.rest.request.RequestGenre
import com.graytsar.wlnupdates.rest.request.RequestTag
import com.graytsar.wlnupdates.rest.response.ResponseAdvancedSearch
import com.graytsar.wlnupdates.rest.response.ResponseGenre
import com.graytsar.wlnupdates.rest.response.ResponseTag
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelAdvancedSearch: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    val listGenre = MutableLiveData<List<ItemGenre>>()
    val containerListGenre = ArrayList<ItemGenre>()

    val listTag = MutableLiveData<List<ItemTag>>()
    val containerListTag = ArrayList<ItemTag>()

    val listResultSearch = MutableLiveData<List<DataAdvancedSearch>>()

    private var requestCallGenre: Call<ResponseGenre>? = null
    private var requestCallTag: Call<ResponseTag>? = null
    private var requestCallSearch: Call<ResponseAdvancedSearch>? = null

    val typeAll = MutableLiveData<Boolean>(true)
    val typeOriginal = MutableLiveData<Boolean>(false)
    val typeTranslated = MutableLiveData<Boolean>(false)

    val sortName = MutableLiveData<Boolean>(true)
    val sortUpdate = MutableLiveData<Boolean>(false)
    val sortChapter = MutableLiveData<Boolean>(false)

    //errors
    val errorResponseGenre = MutableLiveData<ResponseGenre>()
    val errorResponseTag = MutableLiveData<ResponseTag>()
    val errorResponseAdvancedSearch = MutableLiveData<ResponseAdvancedSearch>()
    val failureResponse = MutableLiveData<Throwable>()

    fun getDataGenre() {
        requestCallGenre?.cancel()
        requestCallGenre = RestService.restService.getGenre(RequestGenre())
        isLoading.postValue(true)
        requestCallGenre?.enqueue(object: Callback<ResponseGenre> {
            override fun onResponse(call: Call<ResponseGenre>, response: Response<ResponseGenre>) {
                if(response.isSuccessful){
                    response.body()?.let { responseGenre ->
                        if(!responseGenre.error!!) {
                            onReceivedResultGenre(responseGenre)
                        } else {
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseGenre.postValue(it)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseGenre>, t: Throwable) {
                if(!call.isCanceled) {
                    failureResponse.postValue(t)
                }
                isLoading.postValue(false)
                //Log.d("DBG-Failure:", "restService.getGenre() onFailure")
            }
        })
    }

    fun getDataTag() {
        requestCallTag?.cancel()
        requestCallTag = RestService.restService.getTag(RequestTag())
        isLoading.postValue(true)
        requestCallTag?.enqueue(object: Callback<ResponseTag> {
            override fun onResponse(call: Call<ResponseTag>, response: Response<ResponseTag>) {
                if(response.isSuccessful){
                    response.body()?.let { responseTag ->
                        if(!responseTag.error!!) {
                            onReceivedResultTag(responseTag)
                        } else {
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseTag.postValue(it)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseTag>, t: Throwable) {
                if(!call.isCanceled) {
                    failureResponse.postValue(t)
                }
                isLoading.postValue(false)
                //Log.d("DBG-Failure:", "restService.getTag() onFailure")
            }
        })
    }

    fun getDataAdvancedSearch(requestAdvancedSearch: RequestAdvancedSearch) {
        requestCallSearch?.cancel()
        requestCallSearch = RestService.restService.getAdvancedSearch(requestAdvancedSearch)
        isLoading.postValue(true)
        requestCallSearch?.enqueue(object: Callback<ResponseAdvancedSearch> {
            override fun onResponse(call: Call<ResponseAdvancedSearch>, response: Response<ResponseAdvancedSearch>) {
                if(response.isSuccessful){
                    response.body()?.let { responseSearch ->
                        if(!responseSearch.error!!) {
                            onReceivedResultAdvancedSearch(responseSearch)
                        } else {
                            onErrorReceivedResultAdvancedSearch(responseSearch)
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        onErrorReceivedResultAdvancedSearch(it)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseAdvancedSearch>, t: Throwable) {
                if(!call.isCanceled) {
                    failureResponse.postValue(t)
                }
                isLoading.postValue(false)
                //Log.d("DBG-Failure:", "restService.getAdvancedSearch() onFailure")
            }
        })
    }

    private fun onReceivedResultGenre(result: ResponseGenre){
        containerListGenre.clear()
        result.data?.let { dataGenre ->
            dataGenre.forEach {
                val rList = it as ArrayList<*>

                val id:Int = (rList[0] as Double).toInt()
                val genre:String = rList[1] as String
                val occurrences:Int = (rList[2] as Double).toInt()

                if(occurrences >= 25) {
                    containerListGenre.add(ItemGenre(id, genre, occurrences))
                }
            }
            listGenre.postValue(containerListGenre)

        }
        isLoading.postValue(false)
    }

    private fun onReceivedResultTag(result: ResponseTag){
        containerListTag.clear()
        result.data?.let { dataTag ->
            dataTag.forEach {
                val rList = it as ArrayList<*>

                val id:Int = (rList[0] as Double).toInt()
                val tag:String = rList[1] as String
                val occurrences:Int = (rList[2] as Double).toInt()

                if(occurrences >= 25) {
                    containerListTag.add(ItemTag(id, tag, occurrences))
                }
            }
            listTag.postValue(containerListTag)

        }
        isLoading.postValue(false)
    }

    private fun onReceivedResultAdvancedSearch(result: ResponseAdvancedSearch){
        result.data?.let {
            listResultSearch.postValue(it)
        }

        isLoading.postValue(false)
    }

    private fun onErrorReceivedResultAdvancedSearch(result: ResponseAdvancedSearch) {
        errorResponseAdvancedSearch.postValue(result)
    }
}