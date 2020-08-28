package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.Item

class DataRecent(
    @SerializedName("has_next")
    @Expose
    var hasNext: Boolean? = null,

    @SerializedName("has_prev")
    @Expose
    var hasPrev: Boolean? = null,

    @SerializedName("items")
    @Expose
    var items: List<Item>? = null,

    @SerializedName("next_num")
    @Expose
    var nextNum: Int? = null,

    @SerializedName("pages")
    @Expose
    var pages: Int? = null,

    @SerializedName("per_page")
    @Expose
    var perPage: Int? = null,

    @SerializedName("prev_num")
    @Expose
    var prevNum: Int? = null,

    @SerializedName("total")
    @Expose
    var total: Int? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.createTypedArrayList(Item.CREATOR),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(hasNext)
        writeValue(hasPrev)
        writeTypedList(items)
        writeValue(nextNum)
        writeValue(pages)
        writeValue(perPage)
        writeValue(prevNum)
        writeValue(total)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataRecent> = object : Parcelable.Creator<DataRecent> {
            override fun createFromParcel(source: Parcel): DataRecent = DataRecent(source)
            override fun newArray(size: Int): Array<DataRecent?> = arrayOfNulls(size)
        }
    }
}