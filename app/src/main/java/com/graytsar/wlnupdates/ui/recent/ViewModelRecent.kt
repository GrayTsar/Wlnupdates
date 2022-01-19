package com.graytsar.wlnupdates.ui.recent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService.restService
import com.graytsar.wlnupdates.rest.request.RequestRecent
import com.graytsar.wlnupdates.rest.response.ResponseRecent
import com.graytsar.wlnupdates.utils.ErrorRecentListener
import com.graytsar.wlnupdates.utils.ErrorSearchListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ViewModelRecent: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    var errorListener: ErrorRecentListener? = null
    fun setErrorRecentListener(errorListener: ErrorRecentListener) {
        this.errorListener = errorListener
    }

    private var ps:PagingSourceRecent? = null
    val pager = Pager(PagingConfig(pageSize = 50)) {
        PagingSourceRecent(this).also {
            ps = it
        }
    }.flow.cachedIn(viewModelScope)

    fun refresh(){
        ps?.invalidate()
    }

    private class PagingSourceRecent(private val viewModelRecent: ViewModelRecent): PagingSource<Int, Item>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            try {
                var response:Response<ResponseRecent>? = null
                withContext(Dispatchers.IO)  {
                    response = restService.getRecent(RequestRecent(nextPageNumber)).execute()
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
                            viewModelRecent.errorListener?.onSubmitErrorResponse(body)
                        }
                    }
                } else {
                    //server did not respond
                    viewModelRecent.errorListener?.onSubmitErrorServer(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                viewModelRecent.errorListener?.onSubmitFailure(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                viewModelRecent.errorListener?.onSubmitFailure(e)
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