package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestTag(
    @SerializedName("mode")
    @Expose
    var mode:String = "enumerate-tags")