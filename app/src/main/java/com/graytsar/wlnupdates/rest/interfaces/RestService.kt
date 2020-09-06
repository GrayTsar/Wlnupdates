package com.graytsar.wlnupdates.rest.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestService {
    var restService:WlnupdatesInterface

    init {
        /*
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor() { chain ->
            chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Basic VGVzdFNvbWV0aGluZ0FwaToxMjM0NTY3OA==").build())
        }
        clientBuilder.addInterceptor(logging)
        val client = clientBuilder.build()
         */

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.wlnupdates.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        restService = retrofit.create(WlnupdatesInterface::class.java)
    }

}