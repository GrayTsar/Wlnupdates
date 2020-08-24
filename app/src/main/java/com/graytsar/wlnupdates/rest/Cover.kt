package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cover {
    @SerializedName("chapter")
    @Expose
    var chapter: Any? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("srcfname")
    @Expose
    var srcfname: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("volume")
    @Expose
    var volume: Double? = null
}