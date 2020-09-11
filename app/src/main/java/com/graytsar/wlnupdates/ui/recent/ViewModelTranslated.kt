package com.graytsar.wlnupdates.ui.recent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestOriginal
import com.graytsar.wlnupdates.rest.request.RequestTranslated
import com.graytsar.wlnupdates.rest.response.ResponseOriginal
import com.graytsar.wlnupdates.rest.response.ResponseTranslated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.stream.Collectors

class ViewModelTranslated: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    val errorResponseTranslated = MutableLiveData<ResponseTranslated>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerTranslated = MutableLiveData<Response<ResponseTranslated>>()

    fun search(query: String) {
        pager.map { pagingData ->
            pagingData.filter { item ->
                item.series!!.name!!.contains(query, true)
            }
        }
    }

    val pager = Pager(PagingConfig(pageSize = 50)) {
        pagingSourceTranslated
    }.flow.cachedIn(viewModelScope)

    private val pagingSourceTranslated = object: PagingSource<Int, Item>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            try {
                var response:Response<ResponseTranslated>? = null
                withContext(Dispatchers.IO)  {
                    response = RestService.restService.getTranslated(RequestTranslated(nextPageNumber)).execute()
                }

                if(response!!.isSuccessful) {
                    //server responded
                    response!!.body()?.let { body ->
                        if(!body.error){
                            //no error
                            body.data?.let { data ->
                                val nextPage = if(data.hasNext!!) data.nextNum else null

                                return LoadResult.Page(data = data.items!!, prevKey = null, nextKey = nextPage)
                            }
                        } else {
                            //had error
                            errorResponseTranslated.postValue(body)
                        }
                    }
                } else {
                    //server did not respond
                    errorServerTranslated.postValue(response)
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
    }

    fun setLoadingIndicator(isVisible: Boolean){
        isLoading.postValue(isVisible)
    }
}