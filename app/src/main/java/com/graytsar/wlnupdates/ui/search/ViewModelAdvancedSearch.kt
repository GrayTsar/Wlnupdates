package com.graytsar.wlnupdates.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.stream.Collectors

class ViewModelAdvancedSearch: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    val listResultSearch = MutableLiveData<List<DataAdvancedSearch>>()

    private var requestCallSearch: Call<ResponseAdvancedSearch>? = null

    val typeAll = MutableLiveData<Boolean>(true)
    val typeOriginal = MutableLiveData<Boolean>(false)
    val typeTranslated = MutableLiveData<Boolean>(false)

    val sortName = MutableLiveData<Boolean>(true)
    val sortUpdate = MutableLiveData<Boolean>(false)
    val sortChapter = MutableLiveData<Boolean>(false)

    //errors
    var lastErrorResponseGenre:ResponseGenre? = null
    val errorResponseGenre = MutableLiveData<ResponseGenre>()

    var lastErrorResponseTag:ResponseTag? = null
    val errorResponseTag = MutableLiveData<ResponseTag>()

    var lastErrorResponseAdvancedSearch:ResponseAdvancedSearch? = null
    val errorResponseAdvancedSearch = MutableStateFlow<ResponseAdvancedSearch?>(null)

    var lastErrorServerGenre:Response<ResponseGenre>? = null
    val errorServerGenre = MutableLiveData<Response<ResponseGenre>>()

    var lastErrorServerTag:Response<ResponseTag>? = null
    val errorServerTag = MutableLiveData<Response<ResponseTag>>()

    var lastErrorServerAdvancedSearch:Response<ResponseAdvancedSearch>? = null
    val errorServerAdvancedSearch = MutableLiveData<Response<ResponseAdvancedSearch>>()

    var lastFailureResponse:Throwable? = null
    val failureResponse = MutableLiveData<Throwable>()


    val pagerGenre = Pager(PagingConfig(pageSize = 50)){
        pagingSourceGenre
    }.flow.cachedIn(viewModelScope)

    val pagerTag = Pager(PagingConfig(pageSize = 50)){
        pagingSourceTag
    }.flow.cachedIn(viewModelScope)

    fun getDataAdvancedSearch(requestAdvancedSearch: RequestAdvancedSearch) {
        requestCallSearch?.cancel()
        requestCallSearch = RestService.restService.getAdvancedSearch(requestAdvancedSearch)
        setLoadingIndicator(true)
        requestCallSearch?.enqueue(object: Callback<ResponseAdvancedSearch> {
            override fun onResponse(call: Call<ResponseAdvancedSearch>, response: Response<ResponseAdvancedSearch>) {
                if(response.isSuccessful){
                    response.body()?.let { responseSearch ->
                        if(!responseSearch.error) {
                            onReceivedResultAdvancedSearch(responseSearch)
                        } else {
                            errorResponseAdvancedSearch.value = responseSearch
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseAdvancedSearch.value = it
                    } ?: let {
                        errorServerAdvancedSearch.postValue(response)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                setLoadingIndicator(false)
            }

            override fun onFailure(call: Call<ResponseAdvancedSearch>, t: Throwable) {
                if(!call.isCanceled) {
                    failureResponse.postValue(t)
                }
                setLoadingIndicator(false)
                //Log.d("DBG-Failure:", "restService.getAdvancedSearch() onFailure")
            }
        })
    }

    private fun onReceivedResultAdvancedSearch(result: ResponseAdvancedSearch){
        result.data?.let {
            listResultSearch.postValue(it)
        }
        setLoadingIndicator(false)
    }

    private val pagingSourceGenre = object: PagingSource<Long, ItemGenre>() {
        private val pageSize:Long = 50
        private var response:Response<ResponseGenre>? = null
        private var listSeriesGenre = ArrayList<ItemGenre>()

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ItemGenre> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: pageSize

            try {
                if(response == null) {
                    withContext(Dispatchers.IO)  {
                        response = RestService.restService.getGenre(RequestGenre()).execute()
                    }
                }

                if(response!!.isSuccessful) {
                    //server responded
                    response!!.body()?.let { body ->
                        if(!body.error){
                            //no error
                            return if(listSeriesGenre.isEmpty()) {
                                body.data?.forEach {
                                    val rList = it as ArrayList<*>

                                    val id:Int = (rList[0] as Double).toInt()
                                    val genre:String = rList[1] as String
                                    val occurrences:Int = (rList[2] as Double).toInt()

                                    if(occurrences >= 25) {
                                        listSeriesGenre.add(ItemGenre(id, genre, occurrences))
                                    }
                                }

                                val subList = listSeriesGenre.stream().limit(pageSize).collect(
                                    Collectors.toList())
                                LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                            } else {
                                if(nextPageNumber < listSeriesGenre.size) {
                                    val subList = listSeriesGenre.stream().skip(nextPageNumber - pageSize).limit(nextPageNumber).collect(
                                        Collectors.toList())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                                } else {
                                    val max:Long = listSeriesGenre.size.toLong()
                                    val subList = listSeriesGenre.stream().skip(nextPageNumber - pageSize).limit(max).collect(
                                        Collectors.toList())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = null)
                                }
                            }
                        } else {
                            //had error
                            errorResponseGenre.postValue(body)
                        }
                    }
                } else {
                    //server did not respond
                    errorServerGenre.postValue(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                failureResponse.postValue(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                failureResponse.postValue(e)
                return LoadResult.Error(e)
            }
            return LoadResult.Error(Exception())
        }

        override fun getRefreshKey(state: PagingState<Long, ItemGenre>): Long? {
            return 0
        }

    }

    private val pagingSourceTag = object: PagingSource<Long, ItemTag>() {
        private val pageSize:Long = 100
        private var response:Response<ResponseTag>? = null
        private var listSeriesTag = ArrayList<ItemTag>()

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ItemTag> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: pageSize

            try {
                if(response == null) {
                    withContext(Dispatchers.IO)  {
                        response = RestService.restService.getTag(RequestTag()).execute()
                    }
                }

                if(response!!.isSuccessful) {
                    //server responded
                    response!!.body()?.let { body ->
                        if(!body.error){
                            //no error
                            return if(listSeriesTag.isEmpty()) {
                                body.data?.forEach {
                                    val rList = it as ArrayList<*>

                                    val id:Int = (rList[0] as Double).toInt()
                                    val genre:String = rList[1] as String
                                    val occurrences:Int = (rList[2] as Double).toInt()

                                    if(occurrences >= 25) {
                                        listSeriesTag.add(ItemTag(id, genre, occurrences))
                                    }
                                }

                                val subList = listSeriesTag.stream().limit(pageSize).collect(
                                    Collectors.toList())
                                Log.d("DBG-1", nextPageNumber.toString())
                                LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                            } else {
                                if(nextPageNumber < listSeriesTag.size) {
                                    val subList = listSeriesTag.stream().skip(nextPageNumber - pageSize).limit(nextPageNumber).collect(
                                        Collectors.toList())
                                    Log.d("DBG-2", nextPageNumber.toString())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                                } else {
                                    val max:Long = listSeriesTag.size.toLong()
                                    val subList = listSeriesTag.stream().skip(nextPageNumber - pageSize).limit(max).collect(
                                        Collectors.toList())
                                    Log.d("DBG-3", max.toString())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = null)
                                }
                            }
                        } else {
                            //had error
                            errorResponseTag.postValue(body)
                        }
                    }
                } else {
                    //server did not respond
                    errorServerTag.postValue(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                failureResponse.postValue(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                failureResponse.postValue(e)
                return LoadResult.Error(e)
            }
            return LoadResult.Error(Exception())
        }

        override fun getRefreshKey(state: PagingState<Long, ItemTag>): Long? {
            return 0
        }

    }

    fun setLoadingIndicator(isVisible: Boolean){
        isLoading.postValue(isVisible)
    }
}