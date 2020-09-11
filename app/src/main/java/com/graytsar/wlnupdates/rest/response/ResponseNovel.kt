package com.graytsar.wlnupdates.rest.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.data.DataNovel

class ResponseNovel(
    @SerializedName("data")
    @Expose
    var dataNovel: DataNovel? = null,

    @SerializedName("error")
    @Expose
    var error: Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<DataNovel>(DataNovel::class.java.classLoader),
        source.readValue(Boolean::class.java.classLoader) as Boolean,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(dataNovel, 0)
        writeValue(error)
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ResponseNovel> =
            object : Parcelable.Creator<ResponseNovel> {
                override fun createFromParcel(source: Parcel): ResponseNovel = ResponseNovel(source)
                override fun newArray(size: Int): Array<ResponseNovel?> = arrayOfNulls(size)
            }
    }
}