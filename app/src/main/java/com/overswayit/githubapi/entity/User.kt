package com.overswayit.githubapi.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(@PrimaryKey val id: Int, val login: String, @ColumnInfo(name = "avatar_uri") val avatarUri: String?, var name: String, val company: String?, val bio: String?, val email: String?, val location: String?, val blog: String?)
