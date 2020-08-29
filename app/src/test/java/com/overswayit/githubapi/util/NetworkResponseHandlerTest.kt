package com.overswayit.githubapi.util

import com.overswayit.githubapi.entity.User
import okhttp3.ResponseBody
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test
import retrofit2.Response


class NetworkResponseHandlerTest {

    @Test
    fun userResponseHandler_givenSuccessResponseReturnedUserListIsSameAsGiven() {
        // Given response
        val givenUserList = listOf(User(1, "user", "user", "user", "user", "user", "user", "user", "user", "user"))
        val success = Response.success(givenUserList)

        // When in successful list is user list
        var responseUserList = emptyList<User>()
        NetworkResponseHandler.userResponseHandler(success, {responseUserList = it}, {})

        // Then response user list is not empty
        // and response user list is same as given user list
        assertThat(responseUserList, not(emptyList()))
        assertThat(responseUserList, `is`(givenUserList))
    }

    @Test
    fun userResponseHandler_givenErrorResponseReturnedMessageIsSameAsGiven() {
        // Given response
        val givenError = "I'm a teapot"
        val error = Response.error<String>(418, ResponseBody.create(null, givenError))

        // When in error getting response error
        var responseError = ""
        NetworkResponseHandler.userResponseHandler(error, {}, {responseError = it})

        // Then response error is not null
        // and response error is same as given error
        assertThat(responseError, not(nullValue()))
        assertThat(responseError, `is`(givenError))
    }

}