package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Novel {
    @SerializedName("data")
    @Expose
    var dataNovel: DataNovel? = null

    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: Any? = null
}