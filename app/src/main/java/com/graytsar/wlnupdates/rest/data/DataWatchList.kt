package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.ExtraMetadata


class DataWatchList(
    @SerializedName("extra_metadata")
    @Expose
    var extraMetadata: ExtraMetadata? = null,

    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("rating")
    @Expose
    var rating: Double = 0.0,

    @SerializedName("rating_count")
    @Expose
    var ratingCount: Int = 0,

    @SerializedName("tl_type")
    @Expose
    var tlType: String? = null,

    @SerializedName("agg")
    @Expose
    var agg: Double = 0.0,

    @SerializedName("chp")
    @Expose
    var chp: Double = 0.0,

    @SerializedName("frag")
    @Expose
    var frag: Double = 0.0,

    @SerializedName("vol")
    @Expose
    var vol: Int = 0,

    @SerializedName("date")
    @Expose
    var date: Double = 0.0
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<ExtraMetadata>(ExtraMetadata::class.java.classLoader),
        source.readInt(),
        source.readString(),
        source.readDouble(),
        source.readInt(),
        source.readString(),
        source.readDouble(),
        source.readDouble(),
        source.readDouble(),
        source.readInt(),
        source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(extraMetadata, 0)
        writeInt(id)
        writeString(name)
        writeDouble(rating)
        writeInt(ratingCount)
        writeString(tlType)
        writeDouble(agg)
        writeDouble(chp)
        writeDouble(frag)
        writeInt(vol)
        writeDouble(date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataWatchList> =
            object : Parcelable.Creator<DataWatchList> {
                override fun createFromParcel(source: Parcel): DataWatchList = DataWatchList(source)
                override fun newArray(size: Int): Array<DataWatchList?> = arrayOfNulls(size)
            }
    }
}