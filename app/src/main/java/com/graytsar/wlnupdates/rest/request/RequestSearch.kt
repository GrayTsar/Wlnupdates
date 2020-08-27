package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSearch(
    @SerializedName("id")
    @Expose
    var title:String,
    @SerializedName("mode")
    @Expose
    var mode:String = "search-title") {
}