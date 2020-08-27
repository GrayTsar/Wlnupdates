package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Illustrator(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("illustrator")
    @Expose
    var illustrator: String? = null
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
        writeString(illustrator)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Illustrator> = object : Parcelable.Creator<Illustrator> {
            override fun createFromParcel(source: Parcel): Illustrator = Illustrator(source)
            override fun newArray(size: Int): Array<Illustrator?> = arrayOfNulls(size)
        }
    }
}