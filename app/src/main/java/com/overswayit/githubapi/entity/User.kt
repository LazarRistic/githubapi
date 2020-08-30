package com.overswayit.githubapi.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    val id: Int,
    @PrimaryKey val login: String,
    @ColumnInfo(name = "avatar_url") @SerializedName("avatar_url") val avatarUri: String?,
    var url: String,
    var name: String?,
    val company: String?,
    val bio: String?,
    val email: String?,
    val location: String?,
    val blog: String?,
    val followers: Int?,
    val following: Int?,
    @ColumnInfo(name = "public_repos") @SerializedName("public_repos") val repos: Int?
)
