package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataWatchList


class ResponseWatchList(
    @SerializedName("data")
    @Expose
    var data: List<DataWatchList>? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.createTypedArrayList(DataWatchList.CREATOR),
        1 == source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(data)
        writeInt((if (error) 1 else 0))
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ResponseWatchList> =
            object : Parcelable.Creator<ResponseWatchList> {
                override fun createFromParcel(source: Parcel): ResponseWatchList =
                    ResponseWatchList(source)

                override fun newArray(size: Int): Array<ResponseWatchList?> = arrayOfNulls(size)
            }
    }
}