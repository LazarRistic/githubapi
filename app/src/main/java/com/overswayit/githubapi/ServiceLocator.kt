package com.overswayit.githubapi

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.overswayit.githubapi.api.*
import com.overswayit.githubapi.db.*
import com.overswayit.githubapi.repository.DefaultReposRepository
import com.overswayit.githubapi.repository.DefaultUserRepository
import com.overswayit.githubapi.repository.ReposRepository
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

    @Volatile
    var reposRepository: ReposRepository? = null
        @VisibleForTesting set

    fun provideGithubAPIService(): GitHubAPIService {
        return this.apiService ?: createApiService()
    }

    fun provideReposRepository(context: Context): ReposRepository{
        synchronized(this) {
            return reposRepository ?: createRepoRepository(context)
        }
    }

    fun provideUsersRepository(context: Context): UsersRepository {
        synchronized(this) {
            return usersRepository ?: createUsersRepository(context)
        }
    }

    private fun createRepoRepository(context: Context): ReposRepository {
        val newRepo =
            DefaultReposRepository(createRepoRemoteDataSource(), createRepoLocalDataSource(context))
        reposRepository = newRepo
        return newRepo
    }

    private fun createRepoRemoteDataSource(): ReposRemoteDataSource {
        val apiService = provideGithubAPIService()
        return DefaultReposRemoteDataSource(apiService)
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

    private fun createRepoLocalDataSource(context: Context): ReposLocalDataSource {
        val database = database ?: createDataBase(context)
        return DefaultReposLocalDataSource(database.repoDao())
    }

    private fun createDataBase(context: Context): GitHubAPIDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            GitHubAPIDatabase::class.java,
            "githubapi.db"
        ).fallbackToDestructiveMigration().build()
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