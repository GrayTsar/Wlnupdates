package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.ResultSearch

class DataSearch(
    @SerializedName("cleaned_search")
    @Expose
    var cleanedSearch: String? = null,

    @SerializedName("results")
    @Expose
    var results: List<ResultSearch>? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        ArrayList<ResultSearch>().apply {
            source.readList(
                this,
                ResultSearch::class.java.classLoader
            )
        }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(cleanedSearch)
        writeList(results)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataSearch> = object : Parcelable.Creator<DataSearch> {
            override fun createFromParcel(source: Parcel): DataSearch = DataSearch(source)
            override fun newArray(size: Int): Array<DataSearch?> = arrayOfNulls(size)
        }
    }
}