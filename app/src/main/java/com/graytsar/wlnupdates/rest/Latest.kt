package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Latest(
    @SerializedName("chp")
    @Expose
    var chp: Double? = null,

    @SerializedName("frg")
    @Expose
    var frg: Double? = null,

    @SerializedName("vol")
    @Expose
    var vol: Double? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(chp)
        writeValue(frg)
        writeValue(vol)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Latest> = object : Parcelable.Creator<Latest> {
            override fun createFromParcel(source: Parcel): Latest = Latest(source)
            override fun newArray(size: Int): Array<Latest?> = arrayOfNulls(size)
        }
    }
}