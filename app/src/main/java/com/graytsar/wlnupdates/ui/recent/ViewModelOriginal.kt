package com.graytsar.wlnupdates.ui.recent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestOriginal
import com.graytsar.wlnupdates.rest.response.ResponseOriginal
import com.graytsar.wlnupdates.utils.ErrorOriginalsListener
import com.graytsar.wlnupdates.utils.ErrorSearchListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ViewModelOriginal: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    var errorListener: ErrorOriginalsListener? = null
    fun setErrorOriginalsListener(errorListener: ErrorOriginalsListener) {
        this.errorListener = errorListener
    }

    private var ps: PagingSourceOriginal? = null
    val pager = Pager(PagingConfig(pageSize = 50)) {
        PagingSourceOriginal(this).also {
            ps = it
        }
    }.flow.cachedIn(viewModelScope)

    fun refresh(){
        ps?.invalidate()
    }

    private class PagingSourceOriginal(private val viewModelOriginal: ViewModelOriginal): PagingSource<Int, Item>() {
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
                            viewModelOriginal.errorListener?.onSubmitErrorResponse(body)
                        }
                    }
                } else {
                    //server did not respond
                    viewModelOriginal.errorListener?.onSubmitErrorServer(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                viewModelOriginal.errorListener?.onSubmitFailure(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                viewModelOriginal.errorListener?.onSubmitFailure(e)
                return LoadResult.Error(e)
            }
            return LoadResult.Error(Exception())
        }

        override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
            return 0
        }
    }

    fun setLoadingIndicator(isVisible: Boolean){
        isLoading.postValue(isVisible)
    }
}