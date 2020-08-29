package com.overswayit.githubapi

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.overswayit.githubapi.api.DefaultUsersRemoteDataSource
import com.overswayit.githubapi.api.GitHubAPIService
import com.overswayit.githubapi.api.UsersRemoteDataSource
import com.overswayit.githubapi.db.DefaultUsersLocalDataSource
import com.overswayit.githubapi.db.GitHubAPIDatabase
import com.overswayit.githubapi.db.UsersLocalDataSource
import com.overswayit.githubapi.repository.DefaultUserRepository
import com.overswayit.githubapi.repository.UsersRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A Service Locator for the [UsersRepository]. This is the prod version, with a
 * the "real" [DefaultUsersRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()
    private var database: GitHubAPIDatabase? = null
    private var apiService: GitHubAPIService? = null

    @Volatile
    var usersRepository: UsersRepository? = null
        @VisibleForTesting set

    fun provideGithubAPIService(): GitHubAPIService {
        return this.apiService ?: createApiService()
    }

    fun provideUsersRepository(context: Context): UsersRepository {
        synchronized(this) {
            return usersRepository ?: createUsersRepository(context)
        }
    }

    private fun createUsersRepository(context: Context): UsersRepository {
        val newRepo =
            DefaultUserRepository(createUserRemoteDataSource(), createUserLocalDataSource(context))
        usersRepository = newRepo
        return newRepo
    }

    private fun createUserRemoteDataSource(): UsersRemoteDataSource {
        val apiService = provideGithubAPIService()
        return DefaultUsersRemoteDataSource(apiService)
    }

    private fun createApiService(): GitHubAPIService {
        val result = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubAPIService::class.java)
        apiService = result
        return result
    }

    private fun createUserLocalDataSource(context: Context): UsersLocalDataSource {
        val database = database ?: createDataBase(context)
        return DefaultUsersLocalDataSource(database.userDao())
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
    @Suppress("unused")
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