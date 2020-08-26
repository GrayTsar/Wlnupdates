package com.graytsar.wlnupdates.rest.interfaces

import com.graytsar.wlnupdates.rest.request.RequestNovel
import com.graytsar.wlnupdates.rest.request.RequestOriginal
import com.graytsar.wlnupdates.rest.request.RequestRecent
import com.graytsar.wlnupdates.rest.request.RequestTranslated
import com.graytsar.wlnupdates.rest.response.ResponseNovel
import com.graytsar.wlnupdates.rest.response.ResponseOriginal
import com.graytsar.wlnupdates.rest.response.ResponseRecent
import com.graytsar.wlnupdates.rest.response.ResponseTranslated
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WlnupdatesInterface {

    @POST("api")
    fun getNovel(@Body request:RequestNovel): Call<ResponseNovel>

    @POST("api")
    fun getRecent(@Body request:RequestRecent): Call<ResponseRecent>

    @POST("api")
    fun getTranslated(@Body request:RequestTranslated): Call<ResponseTranslated>

    @POST("api")
    fun getOriginal(@Body request:RequestOriginal): Call<ResponseOriginal>
}