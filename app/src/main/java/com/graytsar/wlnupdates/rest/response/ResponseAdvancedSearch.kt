package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataAdvancedSearch


class ResponseAdvancedSearch(
    @SerializedName("data")
    @Expose
    var data: List<DataAdvancedSearch>? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.createTypedArrayList(DataAdvancedSearch.CREATOR),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(data)
        writeValue(error)
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ResponseAdvancedSearch> =
            object : Parcelable.Creator<ResponseAdvancedSearch> {
                override fun createFromParcel(source: Parcel): ResponseAdvancedSearch =
                    ResponseAdvancedSearch(source)

                override fun newArray(size: Int): Array<ResponseAdvancedSearch?> =
                    arrayOfNulls(size)
            }
    }
}