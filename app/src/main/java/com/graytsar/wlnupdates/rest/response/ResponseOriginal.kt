package com.graytsar.wlnupdates.rest.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataOriginal

class ResponseOriginal {
    @SerializedName("data")
    @Expose
    var data: DataOriginal? = null

    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: Any? = null
}