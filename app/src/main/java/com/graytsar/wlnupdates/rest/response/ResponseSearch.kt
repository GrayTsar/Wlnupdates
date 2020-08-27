package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataSearch

class ResponseSearch(
    @SerializedName("data")
    @Expose
    var dataSearch: DataSearch? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<DataSearch>(DataSearch::class.java.classLoader),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(dataSearch, 0)
        writeValue(error)
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ResponseSearch> =
            object : Parcelable.Creator<ResponseSearch> {
                override fun createFromParcel(source: Parcel): ResponseSearch =
                    ResponseSearch(source)

                override fun newArray(size: Int): Array<ResponseSearch?> = arrayOfNulls(size)
            }
    }
}