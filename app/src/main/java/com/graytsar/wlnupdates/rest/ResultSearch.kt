package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultSearch(
    @SerializedName("match")
    @Expose
    var match: List<List<Any>>? = null,
    @SerializedName("sid")
    @Expose
    var sid: Int? = null
)