package com.graytsar.wlnupdates.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Illustrator {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("illustrator")
    @Expose
    var illustrator: String? = null
}