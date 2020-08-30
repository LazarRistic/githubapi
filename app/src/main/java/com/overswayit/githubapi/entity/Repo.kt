package com.overswayit.githubapi.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

/**
 * Using name/owner_login as primary key instead of id since name/owner_login is always available
 * vs id is not.
 */
@Entity(
    indices = [
        Index("id"),
        Index("owner_login")],
    primaryKeys = ["name", "owner_login"]
)
data class Repo(
    val id: Int,
    val name: String,
    @ColumnInfo(name = "full_name") @SerializedName("full_name") val fullName: String,
    val description: String?,
    @SerializedName("owner") @Embedded(prefix = "owner_") val user: Owner,
    @ColumnInfo(name ="stargazers_count") @SerializedName("stargazers_count") val stars: Int,
    val watchers: Int,
    val forks: Int,
    @ColumnInfo(name = "open_issues") @SerializedName("open_issues") val openIssues: Int
) {
    data class Owner(
        @field:SerializedName("login")
        val login: String,
        @field:SerializedName("url")
        val url: String?
    )
}