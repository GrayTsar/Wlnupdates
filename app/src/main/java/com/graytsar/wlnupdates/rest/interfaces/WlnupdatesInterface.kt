package com.graytsar.wlnupdates.rest.interfaces

import com.graytsar.wlnupdates.rest.request.RequestNovel
import com.graytsar.wlnupdates.rest.response.ResponseNovel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WlnupdatesInterface {

    @POST("api")
    fun getNovel(@Body requestNovel:RequestNovel): Call<ResponseNovel>
}