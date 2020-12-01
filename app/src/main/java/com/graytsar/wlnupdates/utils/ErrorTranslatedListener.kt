package com.graytsar.wlnupdates.utils

import com.graytsar.wlnupdates.rest.response.ResponseSearch
import com.graytsar.wlnupdates.rest.response.ResponseTranslated
import retrofit2.Response

interface ErrorTranslatedListener {

    fun onSubmitErrorResponse(response: ResponseTranslated)

    fun onSubmitFailure(throwable: Throwable)

    fun onSubmitErrorServer(response: Response<ResponseTranslated>?)

}