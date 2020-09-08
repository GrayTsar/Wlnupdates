package com.graytsar.wlnupdates.rest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.WatchList


class DataWatchList(
    @SerializedName("WatchList")
    @Expose
    var watchList: List<List<WatchList>>? = null) {
}