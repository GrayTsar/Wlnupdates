package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestRecent(
    @SerializedName("offset")
    @Expose
    private var offset:Int = 1,
    @SerializedName("mode")
    @Expose
    private var mode:String = "get-releases")