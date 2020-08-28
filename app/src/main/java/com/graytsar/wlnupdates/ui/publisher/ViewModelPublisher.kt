package com.graytsar.wlnupdates.ui.publisher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.Series

class ViewModelPublisher: ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    var list: List<Series?>? = null
}