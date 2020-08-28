package com.graytsar.wlnupdates.ui.illustrator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.Series

class ViewModelIllustrator: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    var list: List<Series?>? = null
}