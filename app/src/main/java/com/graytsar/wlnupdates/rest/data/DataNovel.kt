package com.graytsar.wlnupdates.rest.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.graytsar.wlnupdates.rest.*

class DataNovel(
    @SerializedName("alternatenames")
    @Expose
    var alternatenames: List<String>? = null,

    @SerializedName("authors")
    @Expose
    var authors: List<Author>? = null,

    @SerializedName("covers")
    @Expose
    var covers: List<Cover>? = null,

    @SerializedName("demographic")
    @Expose
    var demographic: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("genres")
    @Expose
    var genres: List<Genre>? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("illustrators")
    @Expose
    var illustrators: List<Illustrator>? = null,

    @SerializedName("latest")
    @Expose
    var latest: Latest? = null,

    @SerializedName("latest_chapter")
    @Expose
    var latestChapter: Double? = null,

    @SerializedName("latest_fragment")
    @Expose
    var latestFragment: Double? = null,

    @SerializedName("latest_published")
    @Expose
    var latestPublished: String? = null,

    @SerializedName("latest_str")
    @Expose
    var latestStr: String? = null,

    @SerializedName("latest_volume")
    @Expose
    var latestVolume: Double? = null,

    @SerializedName("license_en")
    @Expose
    var licenseEn: Boolean? = null,

    @SerializedName("most_recent")
    @Expose
    var mostRecent: String? = null,

    @SerializedName("orig_lang")
    @Expose
    var origLang: String? = null,

    @SerializedName("orig_status")
    @Expose
    var origStatus: String? = null,

    @SerializedName("origin_loc")
    @Expose
    var originLoc: String? = null,

    @SerializedName("progress")
    @Expose
    var progress: Progress? = null,

    @SerializedName("pub_date")
    @Expose
    var pubDate: String? = null,

    @SerializedName("publishers")
    @Expose
    var publishers: List<Publisher>? = null,

    @SerializedName("rating")
    @Expose
    var rating: Rating? = null,

    @SerializedName("rating_count")
    @Expose
    var ratingCount: Int? = null,

    @SerializedName("region")
    @Expose
    var region: String? = null,

    @SerializedName("releases")
    @Expose
    var releases: List<Release>? = null,

    @SerializedName("similar_series")
    @Expose
    var similarSeries: List<SimilarSeries>? = null,

    @SerializedName("tags")
    @Expose
    var tags: List<Tag>? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("tl_type")
    @Expose
    var tlType: String? = null,

    @SerializedName("total_watches")
    @Expose
    var totalWatches: Int? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("watch")
    @Expose
    var watch: Boolean? = null,

    @SerializedName("watchlists")
    @Expose
    var watchlists: List<String>? = null,

    @SerializedName("website")
    @Expose
    var website: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.createStringArrayList(),
        source.createTypedArrayList(Author.CREATOR),
        source.createTypedArrayList(Cover.CREATOR),
        source.readString(),
        source.readString(),
        source.createTypedArrayList(Genre.CREATOR),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.createTypedArrayList(Illustrator.CREATOR),
        source.readParcelable<Latest>(Latest::class.java.classLoader),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readParcelable<Progress>(Progress::class.java.classLoader),
        source.readString(),
        source.createTypedArrayList(Publisher.CREATOR),
        source.readParcelable<Rating>(Rating::class.java.classLoader),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.createTypedArrayList(Release.CREATOR),
        source.createTypedArrayList(SimilarSeries.CREATOR),
        source.createTypedArrayList(Tag.CREATOR),
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.createStringArrayList(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeStringList(alternatenames)
        writeTypedList(authors)
        writeTypedList(covers)
        writeString(demographic)
        writeString(description)
        writeTypedList(genres)
        writeValue(id)
        writeTypedList(illustrators)
        writeParcelable(latest, 0)
        writeValue(latestChapter)
        writeValue(latestFragment)
        writeString(latestPublished)
        writeString(latestStr)
        writeValue(latestVolume)
        writeValue(licenseEn)
        writeString(mostRecent)
        writeString(origLang)
        writeString(origStatus)
        writeString(originLoc)
        writeParcelable(progress, 0)
        writeString(pubDate)
        writeTypedList(publishers)
        writeParcelable(rating, 0)
        writeValue(ratingCount)
        writeString(region)
        writeTypedList(releases)
        writeTypedList(similarSeries)
        writeTypedList(tags)
        writeString(title)
        writeString(tlType)
        writeValue(totalWatches)
        writeString(type)
        writeValue(watch)
        writeStringList(watchlists)
        writeString(website)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataNovel> = object : Parcelable.Creator<DataNovel> {
            override fun createFromParcel(source: Parcel): DataNovel = DataNovel(source)
            override fun newArray(size: Int): Array<DataNovel?> = arrayOfNulls(size)
        }
    }
}