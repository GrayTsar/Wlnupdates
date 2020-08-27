package com.graytsar.wlnupdates.ui.search

import androidx.lifecycle.ViewModel
import com.graytsar.wlnupdates.rest.MatchContent

class ViewModelSearch: ViewModel() {
    var query:String = ""
    val list = ArrayList<MatchContent>()
}