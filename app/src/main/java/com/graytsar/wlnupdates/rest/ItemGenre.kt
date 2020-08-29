package com.graytsar.wlnupdates.rest

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel

class ItemGenre(
    var id: Int,
    var name: String,
    var occurrences: Int
) {
    val isSelected = MutableLiveData<Boolean>(false)
}