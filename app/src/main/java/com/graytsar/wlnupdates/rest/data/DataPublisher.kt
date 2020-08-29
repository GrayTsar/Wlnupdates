package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.SeriesTitle


class DataPublisher(
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("series")
    @Expose
    var series: List<SeriesTitle?>? = null,
    @SerializedName("site")
    @Expose
    var site: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.createTypedArrayList(SeriesTitle.CREATOR),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeTypedList(series)
        writeString(site)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataPublisher> =
            object : Parcelable.Creator<DataPublisher> {
                override fun createFromParcel(source: Parcel): DataPublisher = DataPublisher(source)
                override fun newArray(size: Int): Array<DataPublisher?> = arrayOfNulls(size)
            }
    }
}