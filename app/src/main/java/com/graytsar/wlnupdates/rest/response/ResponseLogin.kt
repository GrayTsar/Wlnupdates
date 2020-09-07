package com.graytsar.wlnupdates.rest.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ResponseLogin(
    @SerializedName("data")
    @Expose
    var data: Any? = null,

    @SerializedName("error")
    @Expose
    var error:Boolean = false,

    @SerializedName("message")
    @Expose
    var message: String? = null) {
}