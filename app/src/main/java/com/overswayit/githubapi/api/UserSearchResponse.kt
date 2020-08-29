package com.overswayit.githubapi.api

import com.google.gson.annotations.SerializedName
import com.overswayit.githubapi.entity.User

/**
 * Simple object to hold user search responses. This is different from the Entity in the database
 * because we are keeping a search result in 1 row and denormalizing list of results into a single
 * column.
 */
data class UserSearchResponse(
    @SerializedName("total_count")
    val total: Int = 0,
    @SerializedName("items")
    val items: List<User>
) {
    @Suppress("unused")
    var nextPage: Int? = null
}
