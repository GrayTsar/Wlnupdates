package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.ExtraMetadata


class DataAdvancedSearch(
    @SerializedName("extra_metadata")
    @Expose
    var extraMetadata: ExtraMetadata? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("latest_published")
    @Expose
    var latestPublished: Double? = null,

    @SerializedName("rating")
    @Expose
    var rating: Double? = null,

    @SerializedName("rating_count")
    @Expose
    var ratingCount: Int? = null,

    @SerializedName("release_count")
    @Expose
    var releaseCount: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("tl_type")
    @Expose
    var tlType: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<ExtraMetadata>(ExtraMetadata::class.java.classLoader),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(extraMetadata, 0)
        writeValue(id)
        writeValue(latestPublished)
        writeValue(rating)
        writeValue(ratingCount)
        writeValue(releaseCount)
        writeString(title)
        writeString(tlType)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataAdvancedSearch> =
            object : Parcelable.Creator<DataAdvancedSearch> {
                override fun createFromParcel(source: Parcel): DataAdvancedSearch =
                    DataAdvancedSearch(source)

                override fun newArray(size: Int): Array<DataAdvancedSearch?> = arrayOfNulls(size)
            }
    }
}