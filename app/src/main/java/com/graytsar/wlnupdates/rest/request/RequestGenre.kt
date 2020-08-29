package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestGenre(
    @SerializedName("mode")
    @Expose
    var mode:String = "enumerate-genres")