package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataGroup


class ResponseGroup(
    @SerializedName("data")
    @Expose
    val data: DataGroup? = null,

    @SerializedName("error")
    @Expose
    val error: Boolean? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<DataGroup>(DataGroup::class.java.classLoader),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(data, 0)
        writeValue(error)
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ResponseGroup> =
            object : Parcelable.Creator<ResponseGroup> {
                override fun createFromParcel(source: Parcel): ResponseGroup = ResponseGroup(source)
                override fun newArray(size: Int): Array<ResponseGroup?> = arrayOfNulls(size)
            }
    }
}