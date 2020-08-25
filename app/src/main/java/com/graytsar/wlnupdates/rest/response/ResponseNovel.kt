package com.graytsar.wlnupdates.rest.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.DataNovel

class ResponseNovel {
    @SerializedName("data")
    @Expose
    var dataNovel: DataNovel? = null

    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}