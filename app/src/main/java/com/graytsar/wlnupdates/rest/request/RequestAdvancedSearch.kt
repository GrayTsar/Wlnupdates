package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAdvancedSearch(
    @SerializedName("title-search-text")
    @Expose
    var titleSearchText:String,

    @SerializedName("series-type")
    @Expose
    var seriesType:LinkedHashMap<String, String>,

    @SerializedName("genre-category")
    @Expose
    var genreCategory:LinkedHashMap<String, String>,

    @SerializedName("tag-category")
    @Expose
    var tagCategory:LinkedHashMap<String, String>,

    @SerializedName("sort-mode")
    @Expose
    var sortMode:String,

    @SerializedName("mode")
    @Expose
    var mode:String = "search-advanced"
)