package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestGroup(
    @SerializedName("id")
    @Expose
    var id:Int,
    @SerializedName("mode")
    @Expose
    var mode:String = "get-group-id")