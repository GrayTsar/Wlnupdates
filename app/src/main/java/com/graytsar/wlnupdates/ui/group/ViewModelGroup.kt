package com.graytsar.wlnupdates.ui.group

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.graytsar.wlnupdates.rest.FeedPaginated
import com.graytsar.wlnupdates.rest.ModelActiveSeries
import com.graytsar.wlnupdates.rest.ReleasesPaginated
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestGroup
import com.graytsar.wlnupdates.rest.response.ResponseGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.stream.Collectors

class ViewModelGroup: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    val name = MutableLiveData<String>("")
    val siteLink = MutableLiveData<String>("")

    var alternateNames = MutableLiveData<List<String?>>()
    var feedPaginated = MutableLiveData<List<FeedPaginated?>>()
    var releasesPaginated = MutableLiveData<List<ReleasesPaginated?>>() //doesn't have title string

    val errorResponseGroup = MutableLiveData<ResponseGroup>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerGroup = MutableLiveData<Response<ResponseGroup>>()

    var pagerGroup: Flow<PagingData<ModelActiveSeries>>? = null
    var pagerGroupFeed = MutableLiveData<Flow<PagingData<FeedPaginated>>>()

    fun createPagerGroupSeries(id:Int) {
        pagerGroup = Pager(PagingConfig(pageSize = 50)){
            PagingSourceGroupSeries(this, id)
        }.flow.cachedIn(viewModelScope)
    }

    class PagingSourceGroupFeed(private val viewModelGroup: ViewModelGroup, private val listFeed:List<FeedPaginated>): PagingSource<Long, FeedPaginated>() {
        private val pageSize:Long = 50

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FeedPaginated> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: pageSize

            return if(params.key == null) {
                val subList = listFeed.stream().limit(pageSize).collect(
                    Collectors.toList())

                Log.d("DBG-1", nextPageNumber.toString())
                LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
            } else {
                if(nextPageNumber < listFeed.size) {
                    val subList = listFeed.stream().skip(nextPageNumber - pageSize).limit(nextPageNumber).collect(
                        Collectors.toList())
                    Log.d("DBG-2", nextPageNumber.toString())
                    LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                } else {
                    val max:Long = listFeed.size.toLong()
                    val subList = listFeed.stream().skip(nextPageNumber - pageSize).limit(max).collect(
                        Collectors.toList())
                    Log.d("DBG-3", max.toString())
                    LoadResult.Page(data = subList, prevKey = null, nextKey = null)
                }
            }
        }

    }

    private class PagingSourceGroupSeries(val viewModelGroup: ViewModelGroup, val id:Int): PagingSource<Long, ModelActiveSeries>() {
        private val pageSize:Long = 50
        private var response:Response<ResponseGroup>? = null
        private var listActiveSeries = ArrayList<ModelActiveSeries>()

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ModelActiveSeries> {
            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: pageSize

            try {
                if(response == null) {
                    withContext(Dispatchers.IO)  {
                        response = RestService.restService.getGroup(RequestGroup(id)).execute()
                    }
                }

                if(response!!.isSuccessful) {
                    //server responded
                    response!!.body()?.let { body ->
                        if(!body.error){
                            //no error
                            return if(listActiveSeries.isEmpty()) {
                                viewModelGroup.name.postValue(body.data!!.group)

                                body.data.activeSeries?.forEach {
                                    listActiveSeries.add(ModelActiveSeries(it.key, it.value))
                                }

                                body.data.feedPaginated?.let {
                                    //listFeedPaginated = ArrayList(it)
                                    viewModelGroup.feedPaginated.postValue(it)
                                    createPagerFeed(it)
                                }

                                val subList = listActiveSeries.stream().limit(pageSize).collect(
                                    Collectors.toList())

                                //Log.d("DBG-1", nextPageNumber.toString())
                                LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                            } else {
                                if(nextPageNumber < listActiveSeries.size) {
                                    val subList = listActiveSeries.stream().skip(nextPageNumber - pageSize).limit(nextPageNumber).collect(
                                        Collectors.toList())
                                    //Log.d("DBG-2", nextPageNumber.toString())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                                } else {
                                    val max:Long = listActiveSeries.size.toLong()
                                    val subList = listActiveSeries.stream().skip(nextPageNumber - pageSize).limit(max).collect(
                                        Collectors.toList())
                                    //Log.d("DBG-3", max.toString())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = null)
                                }
                            }
                        } else {
                            //had error
                            viewModelGroup.errorResponseGroup.postValue(body)
                        }
                    }
                } else {
                    //server did not respond
                    viewModelGroup.errorServerGroup.postValue(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                viewModelGroup.failureResponse.postValue(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                viewModelGroup.failureResponse.postValue(e)
                return LoadResult.Error(e)
            }
            return LoadResult.Error(Exception())
        }

        private fun createPagerFeed(listFeed:List<FeedPaginated>) {
            viewModelGroup.pagerGroupFeed.postValue(Pager(PagingConfig(pageSize = 50)){
                PagingSourceGroupFeed(viewModelGroup, listFeed)
            }.flow.cachedIn(viewModelGroup.viewModelScope))
        }
    }

    fun setLoadingIndicator(isVisible: Boolean){
        isLoading.postValue(isVisible)
    }
}