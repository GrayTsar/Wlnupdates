package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Progress {
    @SerializedName("chp")
    @Expose
    var chp: Double? = null

    @SerializedName("frg")
    @Expose
    var frg: Double? = null

    @SerializedName("vol")
    @Expose
    var vol: Double? = null
}