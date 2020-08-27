package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rating(
    @SerializedName("avg")
    @Expose
    var avg: Double? = null,

    @SerializedName("num")
    @Expose
    var num: Int? = null,

    @SerializedName("user")
    @Expose
    var user: Int? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(avg)
        writeValue(num)
        writeValue(user)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Rating> = object : Parcelable.Creator<Rating> {
            override fun createFromParcel(source: Parcel): Rating = Rating(source)
            override fun newArray(size: Int): Array<Rating?> = arrayOfNulls(size)
        }
    }
}