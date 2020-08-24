package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Genre {
    @SerializedName("genre")
    @Expose
    var genre: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null
}