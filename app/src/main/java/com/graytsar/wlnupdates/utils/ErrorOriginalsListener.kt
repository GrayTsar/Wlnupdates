package com.graytsar.wlnupdates.utils

import com.graytsar.wlnupdates.rest.response.ResponseOriginal
import com.graytsar.wlnupdates.rest.response.ResponseSearch
import retrofit2.Response

interface ErrorOriginalsListener {

    fun onSubmitErrorResponse(response: ResponseOriginal)

    fun onSubmitFailure(throwable: Throwable)

    fun onSubmitErrorServer(response: Response<ResponseOriginal>?)

}