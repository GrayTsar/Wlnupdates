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
    var watchlists: Any? = null,

    @SerializedName("website")
    @Expose
    var website: String? = null
)