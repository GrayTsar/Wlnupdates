package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Item(
    @SerializedName("chapter")
    @Expose
    var chapter: Double? = null,

    @SerializedName("fragment")
    @Expose
    var fragment: Double? = null,

    @SerializedName("postfix")
    @Expose
    var postfix: String? = null,

    @SerializedName("published")
    @Expose
    var published: String? = null,

    @SerializedName("series")
    @Expose
    var series: Series? = null,

    @SerializedName("srcurl")
    @Expose
    var srcurl: String? = null,

    @SerializedName("tlgroup")
    @Expose
    var tlgroup: Tlgroup? = null,

    @SerializedName("volume")
    @Expose
    var volume: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readString(),
        source.readParcelable<Series>(Series::class.java.classLoader),
        source.readString(),
        source.readParcelable<Tlgroup>(Tlgroup::class.java.classLoader),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(chapter)
        writeValue(fragment)
        writeString(postfix)
        writeString(published)
        writeParcelable(series, 0)
        writeString(srcurl)
        writeParcelable(tlgroup, 0)
        writeString(volume)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item = Item(source)
            override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
        }
    }
}