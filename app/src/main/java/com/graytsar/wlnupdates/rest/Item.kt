package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Item {
    @SerializedName("chapter")
    @Expose
    var chapter: Double? = null

    @SerializedName("fragment")
    @Expose
    var fragment: Double? = null

    @SerializedName("postfix")
    @Expose
    var postfix: String? = null

    @SerializedName("published")
    @Expose
    var published: String? = null

    @SerializedName("series")
    @Expose
    var series: Series? = null

    @SerializedName("srcurl")
    @Expose
    var srcurl: String? = null

    @SerializedName("tlgroup")
    @Expose
    var tlgroup: Tlgroup? = null

    @SerializedName("volume")
    @Expose
    var volume: String? = null
}