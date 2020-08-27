package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Genre(
    @SerializedName("genre")
    @Expose
    var genre: String? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents():Int {
        return if(id != null){
            id!!
        } else {
            0
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(genre)
        writeValue(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Genre> = object : Parcelable.Creator<Genre> {
            override fun createFromParcel(source: Parcel): Genre = Genre(source)
            override fun newArray(size: Int): Array<Genre?> = arrayOfNulls(size)
        }
    }
}