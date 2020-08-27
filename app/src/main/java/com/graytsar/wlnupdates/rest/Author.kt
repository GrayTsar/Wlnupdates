package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Author(
    @SerializedName("author")
    @Expose
    var author: String? = null,
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
        writeString(author)
        writeValue(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Author> = object : Parcelable.Creator<Author> {
            override fun createFromParcel(source: Parcel): Author = Author(source)
            override fun newArray(size: Int): Array<Author?> = arrayOfNulls(size)
        }
    }
}