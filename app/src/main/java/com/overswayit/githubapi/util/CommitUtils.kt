package com.overswayit.githubapi.util

import com.google.gson.JsonArray
import com.overswayit.githubapi.service.JsonService

object CommitUtils {

    fun asParents(jsonArray: JsonArray?): ArrayList<Pair<String, String>> {
        val parents = ArrayList<Pair<String, String>>()

        jsonArray?.forEach {
            val json = JsonService.asJsonObject(it)
            val shaFull = JsonService.asString(JsonService.getProperty(json, "sha"), "")
            val shaShort = shaShortener(shaFull)
            parents.add(Pair(shaFull, shaShort))
        }

        return parents
    }

    fun shaShortener(sha: String): String {
        if (sha.length > 6) {
            return sha.substring(0, 7)
        }

        return sha
    }
}