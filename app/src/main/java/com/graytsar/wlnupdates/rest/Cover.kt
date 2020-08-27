package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cover(
    @SerializedName("chapter")
    @Expose
    var chapter: Double? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("srcfname")
    @Expose
    var srcfname: String? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    @SerializedName("volume")
    @Expose
    var volume: Double? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(chapter)
        writeString(description)
        writeValue(id)
        writeString(srcfname)
        writeString(url)
        writeValue(volume)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Cover> = object : Parcelable.Creator<Cover> {
            override fun createFromParcel(source: Parcel): Cover = Cover(source)
            override fun newArray(size: Int): Array<Cover?> = arrayOfNulls(size)
        }
    }
}