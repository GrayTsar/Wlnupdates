package com.graytsar.wlnupdates.ui.novel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelNovel: ViewModel() {
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val rating = MutableLiveData<String>()
    val tlType = MutableLiveData<String>()
    val demographic = MutableLiveData<String>()
    val countryOfOrigin = MutableLiveData<String>()
    val status = MutableLiveData<String>()
    val language = MutableLiveData<String>()
    val licensed = MutableLiveData<String>()

    val author = ArrayList<String>()
    val illustrator = ArrayList<String>()
    val publisher = ArrayList<String>()
    val firstRelease = ArrayList<String>()
    val genres = ArrayList<String>()
    val tags = ArrayList<String>()

    val lastRelease = MutableLiveData<String>()

    val alternativeNames = ArrayList<String>()

    val chapterList = ArrayList<String>()


}