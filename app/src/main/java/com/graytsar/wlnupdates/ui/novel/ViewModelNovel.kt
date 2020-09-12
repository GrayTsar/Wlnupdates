package com.graytsar.wlnupdates.ui.novel

import android.text.TextUtils
import android.text.format.DateUtils
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.*
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestNovel
import com.graytsar.wlnupdates.rest.response.ResponseNovel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@BindingAdapter("android:text")
fun setText(view: TextView, text: String?) {
    view.text = if(text.isNullOrEmpty()){
        return
    } else {
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}



class ViewModelNovel: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    private var requestCall: Call<ResponseNovel>? = null

    var idAuthor:Int = -1

    val title = MutableLiveData<String>("")
    val author = MutableLiveData<String>("")
    val rating = MutableLiveData<String>("") //from 0 - 10

    val description = MutableLiveData<String>("")
    val tlType = MutableLiveData<String>("") //translated, original
    val demographic = MutableLiveData<String>("") //male, female
    val countryOfOrigin = MutableLiveData<String>("") // origin country the novel was released
    val statusReleases = MutableLiveData<String>("") //completed, ongoing, [optional Volume latest]
    val language = MutableLiveData<String>("") //origin language of novel
    val licensed = MutableLiveData<String>("") //licensed by whom
    val firstRelease = MutableLiveData<String>("") //date if first release
    val lastRelease = MutableLiveData<String>("") //date of latest release
    val latestChapter = MutableLiveData<String>("") //name of latest chapter

    // REST data list
    val listAuthor = MutableLiveData<List<Author>>()
    val listChapter = MutableLiveData<List<Release>>()
    val listGenre = MutableLiveData<List<Genre>>()
    val listTag = MutableLiveData<List<Tag>>()
    val listIllustrator = MutableLiveData<List<Illustrator>>()
    val listPublisher = MutableLiveData<List<Publisher>>()
    val listAlternativeNames = MutableLiveData<List<String>>()
    val listSimilarSeries = MutableLiveData<List<SimilarSeries>>()
    val listCovers = MutableLiveData<List<Cover>>()

    val listGroup = MutableLiveData<List<Tlgroup?>>()

    // UI data
    val genre = MutableLiveData<String>()
    val tags = MutableLiveData<String>()
    val illustrator = MutableLiveData<String>()
    val publisher = MutableLiveData<String>()
    val alternativeNames = MutableLiveData<String>()
    val cover = MutableLiveData<String>()
    val group = MutableLiveData<String>()
    val similarSeries = MutableLiveData<String>()

    val errorResponseNovel = MutableLiveData<ResponseNovel>()
    val failureResponse = MutableLiveData<Throwable>()
    val errorServerNovel = MutableLiveData<Response<ResponseNovel>>()

    fun getDataNovel(id:Int) {
        requestCall?.cancel()
        requestCall = RestService.restService.getNovel(RequestNovel(id))
        setLoadingIndicator(true)
        requestCall?.enqueue(object: Callback<ResponseNovel> {
            override fun onResponse(call: Call<ResponseNovel>, response: Response<ResponseNovel>) {
                if(response.isSuccessful){
                    response.body()?.let { responseNovel ->
                        if(!responseNovel.error) {
                            onReceivedResult(responseNovel)
                        } else {
                            errorResponseNovel.postValue(responseNovel)
                            //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                        }
                    }
                } else {
                    response.body()?.let {
                        errorResponseNovel.postValue(it)
                    } ?: let {
                        errorServerNovel.postValue(response)
                    }
                    //Log.d("DBG-Error:", "${response.body()?.error}, ${response.body()?.message}")
                }

                setLoadingIndicator(false)
            }

            override fun onFailure(call: Call<ResponseNovel>, t: Throwable) {
                if(!call.isCanceled){
                    failureResponse.postValue(t)
                }
                setLoadingIndicator(false)
                //Log.d("DBG-Failure:", "restService.getNovel() onFailure")
            }
        })
    }

    private fun onReceivedResult(result: ResponseNovel){
        result.dataNovel?.let { novel ->
            title.postValue(novel.title)

            if(novel.rating?.avg != null && novel.ratingCount != null){
                rating.postValue("${String.format("%.2f", novel.rating?.avg)} (${novel.ratingCount})")
            }

            novel.authors?.let {
                if(it.isNotEmpty()){
                    idAuthor = it[0].id!!
                }

                listAuthor.postValue(it)
                author.postValue(TextUtils.join(", ", it.stream().map { Author -> Author.author }.collect(Collectors.toList())))
            }

            novel.releases?.let {
                listChapter.postValue(it)

                val fGroup = ArrayList<Tlgroup>()
                val lGroup = it.stream().map { Release -> Release.tlgroup!! }.collect(Collectors.toList())
                val map = HashMap<Int, String>()

                lGroup.forEach { _tlGroup ->
                    map[_tlGroup.id!!] = _tlGroup.name!!
                }
                map.forEach { entry ->
                    fGroup.add(Tlgroup(entry.key, entry.value))
                }

                group.postValue(TextUtils.join(", ", fGroup.stream().map { Tlgroup -> Tlgroup.name }.collect(Collectors.toList())))
                listGroup.postValue(fGroup)

            }

            novel.genres?.let {
                listGenre.postValue(it)
                genre.postValue(TextUtils.join(", ", it.stream().map { Genre -> Genre.genre }.collect(Collectors.toList())))
            }

            novel.tags?.let {
                listTag.postValue(it)
                tags.postValue(TextUtils.join(", ", it.stream().map { Tag -> Tag.tag }.collect(Collectors.toList())))
            }

            novel.illustrators?.let {
                listIllustrator.postValue(it)
                illustrator.postValue(TextUtils.join(", ", it.stream().map { Illustrator -> Illustrator.illustrator }.collect(Collectors.toList())))
            }

            novel.publishers?.let {
                listPublisher.postValue(it)
                publisher.postValue(TextUtils.join(", ", it.stream().map { Publisher -> Publisher.publisher }.collect(Collectors.toList())))
            }

            novel.alternatenames?.let {
                listAlternativeNames.postValue(it)

                val altNamesString = StringBuilder()
                it.forEach { name ->
                    altNamesString.append("<br>$name</br>")
                }
                alternativeNames.postValue(altNamesString.toString())
            }

            novel.similarSeries?.let {
                listSimilarSeries.postValue(it)

                val listSimilarSeriesString = StringBuilder()
                it.stream().map { SimilarSeries -> SimilarSeries.title }.collect(Collectors.toList()).forEach { title ->
                    listSimilarSeriesString.append("<br>$title</br>")
                }

                similarSeries.postValue(listSimilarSeriesString.toString())
            }

            novel.covers?.let { list ->
                if(list.isNotEmpty()){
                    cover.postValue(list[0].url)
                }
                listCovers.postValue(list)

            }

            novel.title?.let {
                description.postValue(it)
            }

            novel.tlType?.let {
                tlType.postValue(it)
            }

            novel.demographic?.let {
                demographic.postValue(it)
            }

            novel.originLoc?.let {
                countryOfOrigin.postValue(it)
            }

            novel.origStatus?.let {
                statusReleases.postValue(it)
            }

            novel.origLang?.let {
                language.postValue(it)
            }

            novel.licenseEn?.let {
                if(it){
                    licensed.postValue("Yes")
                } else {
                    licensed.postValue("No")
                }
            }

            novel.pubDate?.let { pubDate ->
                try {
                    val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                    val date = formatter.parse(pubDate)?.time
                    date?.let {
                        val ago = DateUtils.getRelativeTimeSpanString(it, Calendar.getInstance().timeInMillis, DateUtils.DAY_IN_MILLIS)
                        firstRelease.postValue(ago.toString())
                    }
                } catch (e:Exception) {
                    firstRelease.postValue(novel.pubDate)
                }
            }

            lastRelease.postValue(novel.latestPublished)
            novel.latestPublished?.let { latestDate ->
                try {
                    val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                    val date = formatter.parse(latestDate)?.time
                    date?.let {
                        val ago = DateUtils.getRelativeTimeSpanString(it, Calendar.getInstance().timeInMillis, DateUtils.HOUR_IN_MILLIS)
                        lastRelease.postValue(ago.toString())
                    }
                } catch (e:Exception) {
                    lastRelease.postValue(novel.latestPublished)
                }
            }




            latestChapter.postValue("${novel.latestStr} [${novel.releases?.count()}]")

        }
    }

    private fun setLoadingIndicator(isVisible: Boolean){
        isLoading.postValue(isVisible)
    }
}