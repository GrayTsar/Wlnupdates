package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReleasesPaginated(
    @SerializedName("chapter")
    @Expose
    val chapter: Double? = null,

    @SerializedName("fragment")
    @Expose
    val fragment: Double? = null,

    @SerializedName("include")
    @Expose
    val include: Boolean? = null,

    @SerializedName("postfix")
    @Expose
    val postfix: String? = null,

    @SerializedName("published")
    @Expose
    val published: String? = null,

    @SerializedName("srcurl")
    @Expose
    val srcurl: String? = null,

    @SerializedName("volume")
    @Expose
    val volume: Double? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(chapter)
        writeValue(fragment)
        writeValue(include)
        writeString(postfix)
        writeString(published)
        writeString(srcurl)
        writeValue(volume)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ReleasesPaginated> =
            object : Parcelable.Creator<ReleasesPaginated> {
                override fun createFromParcel(source: Parcel): ReleasesPaginated =
                    ReleasesPaginated(source)

                override fun newArray(size: Int): Array<ReleasesPaginated?> = arrayOfNulls(size)
            }
    }
}