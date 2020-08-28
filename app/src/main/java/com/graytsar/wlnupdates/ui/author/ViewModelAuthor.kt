package com.graytsar.wlnupdates.ui.author

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.SeriesAuthor
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestAuthor

class ViewModelAuthor: ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val authorName = MutableLiveData<String>("")
    var list = ArrayList<SeriesAuthor>()


    fun getDataAuthor(id:Int) {
        isLoading.postValue(true)

        val response = RestService.restService.getAuthor(RequestAuthor(id)).execute().body()

        response?.let { responseAuthor ->
            list.clear()

            authorName.postValue(responseAuthor.data?.name)

            response.data?.series?.forEach { item ->
                list.add(item)
            }
        }

        isLoading.postValue(false)
    }
}