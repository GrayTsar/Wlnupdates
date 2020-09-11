package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataPublisher


class ResponsePublisher(
    @SerializedName("data")
    @Expose
    var data: DataPublisher? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<DataPublisher>(DataPublisher::class.java.classLoader),
        source.readValue(Boolean::class.java.classLoader) as Boolean,
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
        val CREATOR: Parcelable.Creator<ResponsePublisher> =
            object : Parcelable.Creator<ResponsePublisher> {
                override fun createFromParcel(source: Parcel): ResponsePublisher =
                    ResponsePublisher(source)

                override fun newArray(size: Int): Array<ResponsePublisher?> = arrayOfNulls(size)
            }
    }
}