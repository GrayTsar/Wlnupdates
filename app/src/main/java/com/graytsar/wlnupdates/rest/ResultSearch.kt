package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultSearch(
    @SerializedName("match")
    @Expose
    var match: List<List<Any>>? = null,
    @SerializedName("sid")
    @Expose
    var sid: Int? = null
) {
}