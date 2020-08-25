package com.graytsar.wlnupdates.ui.novel

import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.graytsar.wlnupdates.rest.*
import com.graytsar.wlnupdates.rest.interfaces.RestService.restService
import com.graytsar.wlnupdates.rest.request.RequestNovel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ViewModelNovel: ViewModel() {
    val title = MutableLiveData<String>()
    val author = MutableLiveData<String>()
    val rating = MutableLiveData<String>() //from 0 - 10

    val description = MutableLiveData<String>()

    val genres = ArrayList<String>()
    val tags = ArrayList<String>()

    val tlType = MutableLiveData<String>() //translated, original
    val demographic = MutableLiveData<String>() //male, female
    val countryOfOrigin = MutableLiveData<String>() // origin country the novel was released
    val statusReleases = MutableLiveData<String>() //completed, ongoing, [optional Volume latest]
    val language = MutableLiveData<String>() //origin language of novel
    val licensed = MutableLiveData<String>() //licensed by whom

    val illustrator = ArrayList<String>()
    val publisher = ArrayList<String>()

    val firstRelease = MutableLiveData<String>() //date if first release

    val lastRelease = MutableLiveData<String>() //date of latest release

    val latestChapter = MutableLiveData<String>()



    val chapterList = ArrayList<String>()

    val listAuthor = ArrayList<Author>()
    val listChapter = ArrayList<Release>()
    val listGenre = ArrayList<Genre>()
    val listTag = ArrayList<Tag>()
    val listIllustrator = ArrayList<Illustrator>()
    val listPublisher = ArrayList<Publisher>()
    val listAlternativeNames = ArrayList<String>()
    val listCovers = ArrayList<Cover>()

    fun getRestData(){
        GlobalScope.launch {
            val novel = restService.getNovel(RequestNovel(3, "get-series-id")).execute().body()
            novel?.dataNovel?.let { novel ->
                title.postValue(novel.title)
                rating.postValue(novel.rating?.avg.toString())

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

                description.postValue(novel.title)
                tlType.postValue(novel.tlType)
                demographic.postValue(novel.demographic)
                countryOfOrigin.postValue(novel.originLoc)
                statusReleases.postValue(novel.origStatus)
                language.postValue(novel.origLang)
                licensed.postValue(novel.licenseEn.toString())


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

            }
        }
    }
}