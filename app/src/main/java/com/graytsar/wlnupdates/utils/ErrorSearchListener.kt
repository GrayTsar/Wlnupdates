package com.graytsar.wlnupdates.utils

import com.graytsar.wlnupdates.rest.response.ResponseSearch
import retrofit2.Response

interface ErrorSearchListener {

    fun onSubmitErrorResponse(response: ResponseSearch)

    fun onSubmitFailure(throwable: Throwable)

    fun onSubmitErrorServer(response: Response<ResponseSearch>?)
}