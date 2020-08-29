package com.overswayit.githubapi.util

import retrofit2.Response

object NetworkResponseHandler {

    fun <T> userResponseHandler(response: Response<T>, successfulFetch: (t: T) -> Unit, failure: (error: String) -> Unit) {
        if (response.isSuccessful) {
            response.body()?.let {
                successfulFetch(it as T)
            }
        } else {
            response.errorBody()?.string()?.let {
                failure(it)
            }
        }
    }

}