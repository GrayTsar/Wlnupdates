package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Release(
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
    var volume: Double? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readString(),
        source.readParcelable<Series>(Series::class.java.classLoader),
        source.readString(),
        source.readParcelable<Tlgroup>(Tlgroup::class.java.classLoader),
        source.readValue(Double::class.java.classLoader) as Double?
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
        writeValue(volume)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Release> = object : Parcelable.Creator<Release> {
            override fun createFromParcel(source: Parcel): Release = Release(source)
            override fun newArray(size: Int): Array<Release?> = arrayOfNulls(size)
        }
    }
}