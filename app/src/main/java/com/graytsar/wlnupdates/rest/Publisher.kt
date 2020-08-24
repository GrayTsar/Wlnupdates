package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Publisher {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("publisher")
    @Expose
    var publisher: String? = null
}