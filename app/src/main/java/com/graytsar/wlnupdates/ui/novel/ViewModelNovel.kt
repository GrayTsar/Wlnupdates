package com.graytsar.wlnupdates.ui.novel

import android.text.format.DateUtils
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.*
import com.graytsar.wlnupdates.rest.interfaces.RestService.restService
import com.graytsar.wlnupdates.rest.request.RequestNovel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@BindingAdapter("android:text")
fun setText(view: TextView, text: String?) {
    view.text = if(text.isNullOrEmpty()){
        return
    } else {
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

class ViewModelNovel: ViewModel() {
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


    val listAuthor = ArrayList<Author>()
    val listChapter = ArrayList<Release>()
    val listGenre = ArrayList<Genre>()
    val listTag = ArrayList<Tag>()
    val listIllustrator = ArrayList<Illustrator>()
    val listPublisher = ArrayList<Publisher>()
    val listAlternativeNames = ArrayList<String>()
    val listCovers = ArrayList<Cover>()

    val genre = MutableLiveData<String>()
    val tags = MutableLiveData<String>()
    val illustrator = MutableLiveData<String>()
    val publisher = MutableLiveData<String>()
    val alternativeNames = MutableLiveData<String>()
    val cover = MutableLiveData<String>()

    fun getRestData(){
        GlobalScope.launch {
            val novel = restService.getNovel(RequestNovel(1196, "get-series-id")).execute().body()
            novel?.dataNovel?.let { novel ->
                title.postValue(novel.title)
                rating.postValue("${novel.rating?.avg.toString()} (${novel.ratingCount})")

                novel.authors?.forEach {
                    listAuthor.add(it)
                }
                var tAuthor:String = ""
                listAuthor.forEach {
                    tAuthor += "${it.author}, "
                }
                author.postValue(tAuthor)

                novel.releases?.forEach {
                    listChapter.add(it)
                }

                novel.genres?.forEach {
                    listGenre.add(it)
                }

                novel.tags?.forEach {
                    listTag.add(it)
                }

                novel.illustrators?.forEach {
                    listIllustrator.add(it)
                }

                novel.publishers?.forEach {
                    listPublisher.add(it)
                }

                novel.alternatenames?.forEach {
                    listAlternativeNames.add(it)
                }

                novel.covers?.forEach {
                    listCovers.add(it)
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

                try {
                    val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                    val date = formatter.parse(novel.pubDate)?.time
                    date?.let {
                        val ago = DateUtils.getRelativeTimeSpanString(it, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS)
                        firstRelease.postValue(ago.toString())
                    }
                } catch (e:Exception) {
                    firstRelease.postValue(novel.pubDate)
                }

                lastRelease.postValue(novel.latestPublished)
                latestChapter.postValue("${novel.latestStr} [${listChapter.count()}]")

                novel.covers?.let {
                    if(it.isNotEmpty()){
                        cover.postValue(it[0].url)
                    }
                }

                populateLists()
            }
        }
    }

    fun populateLists() {
        val maxIterations = 3

        var genreString = ""
        listGenre.forEachIndexed { index, genre ->
            if(index < maxIterations) {
                genreString += "${genre.genre}, "
            } else {
                return@forEachIndexed
            }
        }
        genre.postValue(genreString)

        var tagString = ""
        listTag.forEachIndexed { index, tag ->
            if(index < 3) {
                tagString += "${tag.tag}, "
            } else {
                return@forEachIndexed
            }
        }
        tags.postValue(tagString)

        var illustratorString = ""
        listIllustrator.forEachIndexed { index, illustrator ->
            if(index < maxIterations) {
                illustratorString += "${illustrator.illustrator}, "
            } else {
                return@forEachIndexed
            }
        }
        illustrator.postValue(illustratorString)

        var publisherString = ""
        listPublisher.forEachIndexed { index, publisher ->
            if(index < maxIterations) {
                publisherString += "${publisher.publisher}, "
            } else {
                return@forEachIndexed
            }
        }
        publisher.postValue(publisherString)

        var altNamesString = ""
        listAlternativeNames.forEach {
            altNamesString += "<br>$it</br>"
        }
        alternativeNames.postValue(altNamesString)
    }
}