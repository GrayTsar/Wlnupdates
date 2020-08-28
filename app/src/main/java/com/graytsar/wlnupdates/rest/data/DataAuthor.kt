package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.SeriesAuthor
import com.graytsar.wlnupdates.rest.response.ResponseAuthor

class DataAuthor(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("series")
    @Expose
    var series: List<SeriesAuthor>? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.createTypedArrayList(SeriesAuthor.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeTypedList(series)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataAuthor> = object : Parcelable.Creator<DataAuthor> {
            override fun createFromParcel(source: Parcel): DataAuthor = DataAuthor(source)
            override fun newArray(size: Int): Array<DataAuthor?> = arrayOfNulls(size)
        }
    }
}