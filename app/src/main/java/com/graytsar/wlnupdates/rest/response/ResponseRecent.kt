package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataRecent

class ResponseRecent(
    @SerializedName("data")
    @Expose
    var data: DataRecent? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<DataRecent>(DataRecent::class.java.classLoader),
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
        val CREATOR: Parcelable.Creator<ResponseRecent> =
            object : Parcelable.Creator<ResponseRecent> {
                override fun createFromParcel(source: Parcel): ResponseRecent =
                    ResponseRecent(source)

                override fun newArray(size: Int): Array<ResponseRecent?> = arrayOfNulls(size)
            }
    }
}