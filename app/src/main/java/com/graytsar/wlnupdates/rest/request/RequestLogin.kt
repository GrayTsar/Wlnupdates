package com.graytsar.wlnupdates.rest.request

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class RequestLogin(
    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("mode")
    @Expose
    var mode: String = "do-login",

    @SerializedName("remember_me")
    @Expose
    var isRememberMe:Boolean = true) {
}