package com.graytsar.wlnupdates.rest

import com.graytsar.wlnupdates.rest.request.RequestNovel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitInterface {

    @POST("api")
    fun getNovel(@Body requestNovel:RequestNovel): Call<Novel>
}