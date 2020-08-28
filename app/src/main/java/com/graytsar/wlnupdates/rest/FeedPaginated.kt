package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class FeedPaginated(
    @SerializedName("contents")
    @Expose
    val contents: String? = null,

    @SerializedName("guid")
    @Expose
    val guid: String? = null,

    @SerializedName("linkurl")
    @Expose
    val linkurl: String? = null,

    @SerializedName("published")
    @Expose
    val published: String? = null,

    @SerializedName("region")
    @Expose
    val region: String? = null,

    @SerializedName("srcname")
    @Expose
    val srcname: String? = null,

    @SerializedName("tags")
    @Expose
    val tags: List<String>? = null,

    @SerializedName("title")
    @Expose
    val title: String? = null,

    @SerializedName("updated")
    @Expose
    val updated: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.createStringArrayList(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(contents)
        writeString(guid)
        writeString(linkurl)
        writeString(published)
        writeString(region)
        writeString(srcname)
        writeStringList(tags)
        writeString(title)
        writeString(updated)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FeedPaginated> =
            object : Parcelable.Creator<FeedPaginated> {
                override fun createFromParcel(source: Parcel): FeedPaginated = FeedPaginated(source)
                override fun newArray(size: Int): Array<FeedPaginated?> = arrayOfNulls(size)
            }
    }
}