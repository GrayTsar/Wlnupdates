package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.WatchList
import com.graytsar.wlnupdates.rest.data.DataWatchList


class ResponseWatchList(
    @SerializedName("data")
    @Expose
    var data: List<Any>? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null
)