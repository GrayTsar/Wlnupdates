package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rating {
    @SerializedName("avg")
    @Expose
    var avg: Double? = null

    @SerializedName("num")
    @Expose
    var num: Int? = null

    @SerializedName("user")
    @Expose
    var user: Int? = null
}