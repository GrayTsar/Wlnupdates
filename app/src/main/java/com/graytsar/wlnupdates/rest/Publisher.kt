package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Publisher(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("publisher")
    @Expose
    var publisher: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString()
    )

    override fun describeContents():Int {
        return if(id != null){
            id!!
        } else {
            0
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(publisher)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Publisher> = object : Parcelable.Creator<Publisher> {
            override fun createFromParcel(source: Parcel): Publisher = Publisher(source)
            override fun newArray(size: Int): Array<Publisher?> = arrayOfNulls(size)
        }
    }
}