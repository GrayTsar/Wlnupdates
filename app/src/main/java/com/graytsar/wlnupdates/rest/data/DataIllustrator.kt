package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.Series


class DataIllustrator(
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("series")
    @Expose
    var series: List<Series?>? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.createTypedArrayList(Series.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeTypedList(series)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataIllustrator> =
            object : Parcelable.Creator<DataIllustrator> {
                override fun createFromParcel(source: Parcel): DataIllustrator =
                    DataIllustrator(source)

                override fun newArray(size: Int): Array<DataIllustrator?> = arrayOfNulls(size)
            }
    }
}