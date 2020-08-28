package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataIllustrator


class ResponseIllustrator(
    @SerializedName("data")
    @Expose
    var data: DataIllustrator? = null,
    @SerializedName("error")
    @Expose
    var error: Boolean? = null,
    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<DataIllustrator>(DataIllustrator::class.java.classLoader),
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
        val CREATOR: Parcelable.Creator<ResponseIllustrator> =
            object : Parcelable.Creator<ResponseIllustrator> {
                override fun createFromParcel(source: Parcel): ResponseIllustrator =
                    ResponseIllustrator(source)

                override fun newArray(size: Int): Array<ResponseIllustrator?> = arrayOfNulls(size)
            }
    }
}