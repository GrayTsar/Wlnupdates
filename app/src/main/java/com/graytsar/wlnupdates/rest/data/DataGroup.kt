package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import com.graytsar.wlnupdates.rest.FeedPaginated
import com.graytsar.wlnupdates.rest.ReleasesPaginated


class DataGroup(
    @SerializedName("active-series")
    @Expose
    var activeSeries: LinkedTreeMap<String, String>? = null,

    @SerializedName("alternate-names")
    @Expose
    var alternateNames: List<String>? = null,

    @SerializedName("feed-paginated")
    @Expose
    var feedPaginated: List<FeedPaginated>? = null,

    @SerializedName("group")
    @Expose
    var group: String? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("releases-paginated")
    @Expose
    var releasesPaginated: List<ReleasesPaginated>? = null,

    @SerializedName("site")
    @Expose
    var site: String? = null
) : Parcelable  {
    constructor(source: Parcel) : this(
        source.readSerializable() as LinkedTreeMap<String, String>?,
        ArrayList<String>().apply { source.readList(this, String::class.java.classLoader) },
        source.createTypedArrayList(FeedPaginated.CREATOR),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.createTypedArrayList(ReleasesPaginated.CREATOR),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeSerializable(activeSeries)
        writeList(alternateNames)
        writeTypedList(feedPaginated)
        writeString(group)
        writeValue(id)
        writeTypedList(releasesPaginated)
        writeString(site)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataGroup> = object : Parcelable.Creator<DataGroup> {
            override fun createFromParcel(source: Parcel): DataGroup = DataGroup(source)
            override fun newArray(size: Int): Array<DataGroup?> = arrayOfNulls(size)
        }
    }
}