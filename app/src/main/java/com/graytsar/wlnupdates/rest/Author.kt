package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Author {
    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null
}