package com.overswayit.githubapi.entity

import com.google.gson.JsonObject
import com.overswayit.githubapi.service.JsonService.*
import com.overswayit.githubapi.util.CommitUtils.asParents
import com.overswayit.githubapi.util.CommitUtils.shaShortener
import com.overswayit.githubapi.util.DateUtils
import java.util.*

class Commit(json: JsonObject) {

    val shaShort: String
    val authorLogin: String
    val authorEmail: String
    val authorDate: Date?
    val authorAvatarUrl: String
    val committerLogin: String
    val committerEmail: String
    val message: String
    val parents: List<Pair<String, String>>

    init {
        val shaFull = asString(getProperty(json, "sha"), "")
        shaShort = shaShortener(shaFull)
        authorLogin = asString(getProperty(json, "author", "login"), "")
        authorEmail = asString(getProperty(json, "commit", "author", "email"), "")
        val mDAte = asString(getProperty(json, "commit", "author", "date"), "")
        authorDate = DateUtils.fromString(mDAte)
        authorAvatarUrl = asString(getProperty(json, "author", "avatar_url"), "")
        committerLogin = asString(getProperty(json, "committer", "login"), "")
        committerEmail = asString(getProperty(json, "commit", "committer", "email"), "")
        message = asString(getProperty(json, "commit", "message"), "")
        parents = asParents(asJsonArray(getProperty(json, "parents")))
    }
}