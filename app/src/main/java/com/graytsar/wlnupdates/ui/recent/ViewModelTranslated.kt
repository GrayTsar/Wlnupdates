package com.graytsar.wlnupdates.ui.recent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.Item
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestTranslated

class ViewModelTranslated: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    var currentPage:Int = 0

    var hasNext: Boolean = false
    var hasPrev: Boolean = false

    var nextNum: Int = 0
    var prevNum: Int = 0

    val items = ArrayList<Item>()


    fun getTranslatedData(offset:Int = 1){
        if(currentPage == offset) {
            return
        }
        isLoading.postValue(true)

        val response = RestService.restService.getTranslated(RequestTranslated(offset = offset)).execute().body()
        response?.data?.let { data ->
            data.hasNext?.let {
                hasNext = it
            }
            data.hasPrev?.let {
                hasPrev = it
            }
            data.nextNum?.let {
                nextNum = it
            }
            data.prevNum?.let {
                prevNum = it
            }

            data.items?.forEach {
                items.add(it)
            }
        }

        currentPage = offset
        isLoading.postValue(false)

    }
}