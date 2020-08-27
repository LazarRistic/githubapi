package com.overswayit.githubapi

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.overswayit.githubapi.repository.DefaultUserRepository
import com.overswayit.githubapi.repository.UsersRepository
import com.overswayit.githubapi.db.GitHubAPIDatabase
import com.overswayit.githubapi.db.UsersLocalDataSource
import com.overswayit.githubapi.api.UsersRemoteDataSource
import com.overswayit.githubapi.db.DefaultUsersLocalDataSource
import kotlinx.coroutines.runBlocking

/**
 * A Service Locator for the [UsersRepository]. This is the prod version, with a
 * the "real" [UsersRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()
    private var database: GitHubAPIDatabase? = null

    @Volatile
    var usersRepository: UsersRepository? = null
        @VisibleForTesting set

    fun provideUsersRepository(context: Context): UsersRepository {
        synchronized(this) {
            return usersRepository ?: createUsersRepository(context)
        }
    }

    private fun createUsersRepository(context: Context): UsersRepository {
        val newRepo = DefaultUserRepository(UsersRemoteDataSource(), createUserLocalDataSource(context))
        usersRepository = newRepo
        return newRepo
    }

    private fun createUserLocalDataSource(context: Context): DefaultUsersLocalDataSource {
        val database = database ?: createDataBase(context)
        return UsersLocalDataSource(database.userDao())
    }

    private fun createDataBase(context: Context): GitHubAPIDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            GitHubAPIDatabase::class.java,
            "githubapi.db"
        ).build()
        database = result
        return result
    }

    /**
     *  Clear all data to avoid test pollution
     */
    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            usersRepository = null
        }
    }
}