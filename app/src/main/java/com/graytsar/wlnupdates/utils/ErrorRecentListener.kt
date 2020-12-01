package com.graytsar.wlnupdates.utils

import com.graytsar.wlnupdates.rest.response.ResponseRecent
import com.graytsar.wlnupdates.rest.response.ResponseSearch
import retrofit2.Response

interface ErrorRecentListener {

    fun onSubmitErrorResponse(response: ResponseRecent)

    fun onSubmitFailure(throwable: Throwable)

    fun onSubmitErrorServer(response: Response<ResponseRecent>?)

}