package com.graytsar.wlnupdates.rest.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTag (
    @SerializedName("data")
    @Expose
    var data: List<Any>? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null
)