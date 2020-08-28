package com.graytsar.wlnupdates.ui.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.internal.LinkedTreeMap
import com.graytsar.wlnupdates.rest.FeedPaginated
import com.graytsar.wlnupdates.rest.ReleasesPaginated
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestGroup

class ViewModelGroup: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val group = MutableLiveData<String>("")
    val siteLink = MutableLiveData<String>("")


    var activeSeries: LinkedTreeMap<String, String>? = null
    var alternateNames: List<String?>? = null
    var feedPaginated: List<FeedPaginated?>? = null
    var releasesPaginated: List<ReleasesPaginated?>? = null

    fun getDataGroup(id:Int){
        isLoading.postValue(true)

        val result = RestService.restService.getGroup(RequestGroup(id)).execute().body()
        result?.data?.let { data ->

            group.postValue(data.group)

            siteLink.postValue(data.site)

            data.activeSeries?.let {
                activeSeries = it
            }
            data.alternateNames?.let {
                alternateNames = it
            }
            data.feedPaginated?.let {
                feedPaginated = it
            }
            data.releasesPaginated?.let {
                releasesPaginated = it
            }
        }

        isLoading.postValue(false)
    }
}