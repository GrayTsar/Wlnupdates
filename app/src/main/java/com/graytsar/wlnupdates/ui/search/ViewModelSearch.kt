package com.graytsar.wlnupdates.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.graytsar.wlnupdates.rest.MatchContent
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestSearch
import com.graytsar.wlnupdates.rest.response.ResponseSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.stream.Collectors

class ViewModelSearch: ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)

    val errorResponseSearch = MutableLiveData<ResponseSearch>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerSearch = MutableLiveData<Response<ResponseSearch>>()

    private var ps: PagingSourceSearch? = null
    private var query:String = ""
    var pagerSearch =  Pager(PagingConfig(pageSize = 50)){
        PagingSourceSearch(this, query).also {
            ps = it
        }
    }.flow.cachedIn(viewModelScope)

    fun doSearch(query:String){
        this.query = query
        ps?.invalidate()
    }

    private class PagingSourceSearch(private val viewModelSearch:ViewModelSearch, private val query: String): PagingSource<Long, MatchContent>() {
        private val pageSize:Long = 50
        private var response:Response<ResponseSearch>? = null
        private val listSearch = ArrayList<MatchContent>()

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MatchContent> {
            if(query.length < 2) {
                return LoadResult.Page(data = ArrayList<MatchContent>(), null, null)
            }

            // Load page 1 if undefined.
            val nextPageNumber = params.key ?: pageSize

            try {
                if(response == null) {
                    withContext(Dispatchers.IO)  {
                        response = RestService.restService.getSearch(RequestSearch(query)).execute()
                    }
                }

                if(response!!.isSuccessful) {
                    //server responded
                    response!!.body()?.let { body ->
                        if(!body.error){
                            //no error
                            return if(params.key == null) {
                                val resultSearch = body.dataSearch!!.results

                                resultSearch!!.forEach { search ->
                                    search.match?.forEach { item ->
                                        listSearch.add(MatchContent(search.sid!!, item[0] as Double, item[1] as String))
                                    }
                                }

                                val subList = listSearch.stream().limit(pageSize).collect(Collectors.toList())
                                LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                            } else {
                                if(nextPageNumber < listSearch.size) {
                                    val subList = listSearch.stream().skip(nextPageNumber - pageSize).limit(nextPageNumber).collect(Collectors.toList())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = nextPageNumber + pageSize)
                                } else {
                                    val max:Long = listSearch.size.toLong()
                                    val subList = listSearch.stream().skip(nextPageNumber - pageSize).limit(max).collect(Collectors.toList())
                                    LoadResult.Page(data = subList, prevKey = null, nextKey = null)
                                }
                            }
                        } else {
                            //had error
                            viewModelSearch.errorResponseSearch.postValue(body)
                        }
                    }
                } else {
                    //server did not respond
                    viewModelSearch.errorServerSearch.postValue(response)
                }
            } catch(e: IOException) {
                // IOException for network failures.
                viewModelSearch.failureResponse.postValue(e)
                return LoadResult.Error(e)
            } catch(e: HttpException) {
                // HttpException for any non-2xx HTTP status codes.
                viewModelSearch.failureResponse.postValue(e)
                return LoadResult.Error(e)
            }
            return LoadResult.Error(Exception())
        }
    }

    fun setLoadingIndicator(isVisible: Boolean){
        isLoading.postValue(isVisible)
    }
}