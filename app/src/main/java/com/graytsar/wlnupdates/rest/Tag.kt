package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tag(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("tag")
    @Expose
    var tag: String? = null
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
        writeString(tag)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Tag> = object : Parcelable.Creator<Tag> {
            override fun createFromParcel(source: Parcel): Tag = Tag(source)
            override fun newArray(size: Int): Array<Tag?> = arrayOfNulls(size)
        }
    }
}