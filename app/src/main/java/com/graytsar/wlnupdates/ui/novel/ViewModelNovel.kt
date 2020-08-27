package com.graytsar.wlnupdates.ui.novel

import android.text.TextUtils
import android.text.format.DateUtils
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.*
import com.graytsar.wlnupdates.rest.interfaces.RestService.restService
import com.graytsar.wlnupdates.rest.request.RequestNovel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
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
    val isLoading = MutableLiveData<Boolean>(false)

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
    val listAuthor = ArrayList<Author>()
    val listChapter = ArrayList<Release>()
    val listGenre = ArrayList<Genre>()
    val listTag = ArrayList<Tag>()
    val listIllustrator = ArrayList<Illustrator>()
    val listPublisher = ArrayList<Publisher>()
    val listAlternativeNames = ArrayList<String>()
    val listCovers = ArrayList<Cover>()

    // UI data
    val genre = MutableLiveData<String>()
    val tags = MutableLiveData<String>()
    val illustrator = MutableLiveData<String>()
    val publisher = MutableLiveData<String>()
    val alternativeNames = MutableLiveData<String>()
    val cover = MutableLiveData<String>()

    fun getRestData(id:Int){
        isLoading.postValue(true)
        clearLists()

        val novel = restService.getNovel(RequestNovel(id)).execute().body()
        novel?.dataNovel?.let { novel ->
            title.postValue(novel.title)

            if(novel.rating?.avg != null && novel.ratingCount != null){
                rating.postValue("${String.format("%.2f", novel.rating?.avg)} (${novel.ratingCount})")
            }

            novel.authors?.forEach {
                listAuthor.add(it)
            }

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

            novel.covers?.let { list ->
                if(list.isNotEmpty()){
                    cover.postValue(list[0].url)
                }
                list.forEach { cover ->
                    listCovers.add(cover)
                }
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
                        val ago = DateUtils.getRelativeTimeSpanString(it, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS)
                        firstRelease.postValue(ago.toString())
                    }
                } catch (e:Exception) {
                    firstRelease.postValue(novel.pubDate)
                }
            }

            lastRelease.postValue(novel.latestPublished)
            latestChapter.postValue("${novel.latestStr} [${listChapter.count()}]")

            populateUiLists()
        }

        isLoading.postValue(false)
    }

    private fun populateUiLists() {
        author.postValue(TextUtils.join(", ", listAuthor.stream().map { Author -> Author.author }.collect(Collectors.toList())))

        genre.postValue(TextUtils.join(", ", listGenre.stream().map { Genre -> Genre.genre }.collect(Collectors.toList())))

        tags.postValue(TextUtils.join(", ", listTag.stream().map { Tag -> Tag.tag }.collect(Collectors.toList())))

        illustrator.postValue(TextUtils.join(", ", listIllustrator.stream().map { Illustrator -> Illustrator.illustrator }.collect(Collectors.toList())))

        publisher.postValue(TextUtils.join(", ", listPublisher.stream().map { Publisher -> Publisher.publisher }.collect(Collectors.toList())))

        var altNamesString = ""
        listAlternativeNames.forEach {
            altNamesString += "<br>$it</br>"
        }
        alternativeNames.postValue(altNamesString)
    }

    private fun clearLists(){
        listAuthor.clear()
        listChapter.clear()
        listGenre.clear()
        listTag.clear()
        listIllustrator.clear()
        listPublisher.clear()
        listAlternativeNames.clear()
        listCovers.clear()

    }
}