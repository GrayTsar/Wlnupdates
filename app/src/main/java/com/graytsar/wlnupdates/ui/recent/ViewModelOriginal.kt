package com.graytsar.wlnupdates.ui.recent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestOriginal
import com.graytsar.wlnupdates.rest.response.ResponseOriginal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ViewModelOriginal: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    val errorResponseOriginal = MutableLiveData<ResponseOriginal>()
    val errorServerOriginal = MutableLiveData<Response<ResponseOriginal>>()
    val failureResponse = MutableLiveData<Throwable>()

    val pager = Pager(PagingConfig(pageSize = 50)) {
        pagingSourceOriginal
    }.flow.cachedIn(viewModelScope)

    private val pagingSourceOriginal = object: PagingSource<Int, Item>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            try {
                var response:Response<ResponseOriginal>? = null
                withContext(Dispatchers.IO)  {
                    response = RestService.restService.getOriginal(RequestOriginal(nextPageNumber)).execute()
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
                            errorResponseOriginal.postValue(body)
                        }
                    }
                } else {
                    //server did not respond
                    errorServerOriginal.postValue(response)
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