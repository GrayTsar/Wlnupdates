package com.graytsar.wlnupdates.rest.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestService {
    lateinit var restService:WlnupdatesInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.wlnupdates.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        restService = retrofit.create(WlnupdatesInterface::class.java)
    }

}