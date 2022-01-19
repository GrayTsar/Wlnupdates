package com.graytsar.wlnupdates.ui.recent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestTranslated
import com.graytsar.wlnupdates.rest.response.ResponseTranslated
import com.graytsar.wlnupdates.utils.ErrorSearchListener
import com.graytsar.wlnupdates.utils.ErrorTranslatedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ViewModelTranslated: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    var errorListener: ErrorTranslatedListener? = null
    fun setErrorTranslatedListener(errorListener: ErrorTranslatedListener) {
        this.errorListener = errorListener
    }

    fun search(query: String) {
        pager.map { pagingData ->
            pagingData.filter { item ->
                item.series!!.name!!.contains(query, true)
            }
        }
    }

    private var ps: PagingSourceTranslated? = null
    val pager = Pager(PagingConfig(pageSize = 50)) {
        PagingSourceTranslated(this).also {
            ps = it
        }
    }.flow.cachedIn(viewModelScope)

    fun refresh() {
        ps?.invalidate()
    }

    private class PagingSourceTranslated(private val viewModelTranslated: ViewModelTranslated): PagingSource<Int, Item>() {

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
                            viewModelTranslated.errorListener?.onSubmitErrorResponse(body)
                        }
                    }
                } else {
                    //server did not respond
                    viewModelTranslated.errorListener?.onSubmitErrorServer(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                viewModelTranslated.errorListener?.onSubmitFailure(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                viewModelTranslated.errorListener?.onSubmitFailure(e)
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