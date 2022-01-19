package com.graytsar.wlnupdates.ui.publisher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.graytsar.wlnupdates.rest.SeriesTitle
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestPublisher
import com.graytsar.wlnupdates.rest.response.ResponsePublisher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.stream.Collectors

class ViewModelPublisher: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    val name = MutableLiveData<String>("")

    val errorResponsePublisher = MutableLiveData<ResponsePublisher>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerPublisher = MutableLiveData<Response<ResponsePublisher>>()

    var pagerPublisher: Flow<PagingData<SeriesTitle>>? = null

    fun createPager(id:Int) {
        pagerPublisher = Pager(PagingConfig(pageSize = 50)){
            PagingSourcePublisher(this, id)
        }.flow.cachedIn(viewModelScope)
    }

    private class PagingSourcePublisher(val viewModelPublisher: ViewModelPublisher, val id:Int): PagingSource<Long, SeriesTitle>() {
        private val pageSize:Long = 50
        private var response:Response<ResponsePublisher>? = null
        private var listSeriesTitle:List<SeriesTitle>? = null

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, SeriesTitle> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: pageSize

            try {
                if(response == null) {
                    withContext(Dispatchers.IO)  {
                        response = RestService.restService.getPublisher(RequestPublisher(id)).execute()
                    }
                }

                if(response!!.isSuccessful) {
                    //server responded
                    response!!.body()?.let { body ->
                        if(!body.error){
                            //no error
                            return if(listSeriesTitle == null) {
                                viewModelPublisher.name.postValue(body.data!!.name)
                                listSeriesTitle = body.data!!.series
                                val subList = listSeriesTitle!!.stream().limit(pageSize).collect(Collectors.toList())
                                LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                            } else {
                                if(nextPageNumber < listSeriesTitle!!.size) {
                                    val subList = listSeriesTitle!!.stream().skip(nextPageNumber - pageSize).limit(nextPageNumber).collect(Collectors.toList())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                                } else {
                                    val max:Long = listSeriesTitle!!.size.toLong()
                                    val subList = listSeriesTitle!!.stream().skip(nextPageNumber - pageSize).limit(max).collect(Collectors.toList())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = null)
                                }
                            }
                        } else {
                            //had error
                            viewModelPublisher.errorResponsePublisher.postValue(body)
                        }
                    }
                } else {
                    //server did not respond
                    viewModelPublisher.errorServerPublisher.postValue(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                viewModelPublisher.failureResponse.postValue(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                viewModelPublisher.failureResponse.postValue(e)
                return LoadResult.Error(e)
            }
            return LoadResult.Error(Exception())
        }

        override fun getRefreshKey(state: PagingState<Long, SeriesTitle>): Long? {
            return 0
        }

    }

    fun setLoadingIndicator(isVisible: Boolean){
        isLoading.postValue(isVisible)
    }
}