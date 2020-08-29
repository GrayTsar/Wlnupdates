package com.graytsar.wlnupdates.rest

import androidx.lifecycle.MutableLiveData

class ItemTag(
    var id: Int,
    var name: String,
    var occurrences: Int
) {
    val isSelected = MutableLiveData<Boolean>(false)
}