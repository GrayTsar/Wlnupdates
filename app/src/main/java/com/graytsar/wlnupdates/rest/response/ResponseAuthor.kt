package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataAuthor

class ResponseAuthor(
    @SerializedName("data")
    @Expose
    var data: DataAuthor? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<DataAuthor>(DataAuthor::class.java.classLoader),
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
        val CREATOR: Parcelable.Creator<ResponseAuthor> =
            object : Parcelable.Creator<ResponseAuthor> {
                override fun createFromParcel(source: Parcel): ResponseAuthor =
                    ResponseAuthor(source)

                override fun newArray(size: Int): Array<ResponseAuthor?> = arrayOfNulls(size)
            }
    }
}