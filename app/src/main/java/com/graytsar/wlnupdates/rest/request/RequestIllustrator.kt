package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestIllustrator(
    @SerializedName("id")
    @Expose
    var id:Int,
    @SerializedName("mode")
    @Expose
    var mode:String = "get-artist-id")