package com.graytsar.wlnupdates.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.Item

class DataTranslated {
    @SerializedName("has_next")
    @Expose
    var hasNext: Boolean? = null

    @SerializedName("has_prev")
    @Expose
    var hasPrev: Boolean? = null

    @SerializedName("items")
    @Expose
    var items: List<Item>? = null

    @SerializedName("next_num")
    @Expose
    var nextNum: Int? = null

    @SerializedName("pages")
    @Expose
    var pages: Int? = null

    @SerializedName("per_page")
    @Expose
    var perPage: Int? = null

    @SerializedName("prev_num")
    @Expose
    var prevNum: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null
}