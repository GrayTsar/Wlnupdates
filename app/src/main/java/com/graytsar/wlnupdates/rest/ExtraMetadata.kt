package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ExtraMetadata(
    @SerializedName("is_yaoi")
    @Expose
    var isYaoi: Boolean? = null,

    @SerializedName("is_yuri")
    @Expose
    var isYuri: Boolean? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(isYaoi)
        writeValue(isYuri)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ExtraMetadata> =
            object : Parcelable.Creator<ExtraMetadata> {
                override fun createFromParcel(source: Parcel): ExtraMetadata = ExtraMetadata(source)
                override fun newArray(size: Int): Array<ExtraMetadata?> = arrayOfNulls(size)
            }
    }
}