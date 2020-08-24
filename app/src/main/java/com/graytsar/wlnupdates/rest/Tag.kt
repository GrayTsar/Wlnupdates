package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tag {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("tag")
    @Expose
    var tag: String? = null
}