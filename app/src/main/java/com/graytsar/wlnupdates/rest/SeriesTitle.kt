package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SeriesTitle(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(title)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SeriesTitle> = object : Parcelable.Creator<SeriesTitle> {
            override fun createFromParcel(source: Parcel): SeriesTitle = SeriesTitle(source)
            override fun newArray(size: Int): Array<SeriesTitle?> = arrayOfNulls(size)
        }
    }
}