package com.graytsar.wlnupdates.rest.interfaces

import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.cookieSession
import com.graytsar.wlnupdates.keyCookieDomain
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestService {
    var restService:WlnupdatesInterface
    var sharedPreference:SharedPreferences? = null

    init {
        val url = HttpUrl.Builder().scheme("https").host(keyCookieDomain).addPathSegment("api").build()
        val persistedCookie = if(cookieSession != null) {
            Cookie.parse(url, cookieSession!!)
        } else {
            null
        }

        val cookieJar = object:CookieJar {
            val cookieStore = HashMap<HttpUrl, MutableList<Cookie>>()

            init {
                persistedCookie?.let { persistedCookie ->
                    cookieStore[url]?.let {
                        cookieStore[url]!!.add(persistedCookie)
                    } ?: let {
                        val list = ArrayList<Cookie>()
                        list.add(persistedCookie)
                        cookieStore[url] = list
                    }
                }
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookies.forEach {
                    if(it.persistent){
                        cookieSession = it.toString()
                        sharedPreference?.let { sharedPreferences ->
                            val edit = sharedPreferences.edit()
                            edit.putString(keyCookieDomain, cookieSession)
                            edit.commit()
                        }
                    }
                }

                cookieStore[url] = cookies.toMutableList()
            }

            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                //val cookies = cookieStore[url]
                return cookieStore[url] ?: ArrayList<Cookie>()
            }

        }


        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.cookieJar(cookieJar)
        clientBuilder.addInterceptor(loggingInterceptor)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.wlnupdates.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()

        restService = retrofit.create(WlnupdatesInterface::class.java)
    }

}