package com.overswayit.githubapi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.entity.User

/**
 * The Room Database that contains the User table.
 *
 * ToDo: Set exportSchema to true
 */
@Database(entities = [User::class, Repo::class], version = 1, exportSchema = false)
abstract class GitHubAPIDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
}