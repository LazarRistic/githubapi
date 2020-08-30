package com.overswayit.githubapi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class DefaultReposLocalDataSourceTest {

    private lateinit var localDataSource: DefaultReposLocalDataSource
    private lateinit var database: GitHubAPIDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), GitHubAPIDatabase::class.java).allowMainThreadQueries().build()
        localDataSource = DefaultReposLocalDataSource(database.repoDao())
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insert_observeRepo_resultIsSameAsInsertedRepo() = runBlocking {
        // Given a new repo saved in database
        val insertedOwner = Repo.Owner("login", "laza")
        val insertedRepo = Repo(1, "repo", "repo", "repo", insertedOwner, 1, 1, 1, 1)
        database.repoDao().insert(insertedRepo)

        // When observe repo by owner login and repos name
        val observedRepo = localDataSource.observeRepo(insertedOwner.login, insertedRepo.name).getOrAwaitValue()

        // Then observed repo is same as inserted repo
        assertThat(observedRepo, `is`(notNullValue()))
        assertThat(observedRepo, `is`(insertedRepo))
    }

    @Test
    fun deleteAll_observeRepos_resultEmptyList() = runBlocking {
        // Given three new repos in a database
        val insertedOwner = Repo.Owner("login", "laza")
        val insertedRepo = Repo(1, "repo", "repo", "repo", insertedOwner, 1, 1, 1, 1)
        val insertedRepo2 = Repo(2, "repo2", "repo2", "repo2", insertedOwner, 2, 2, 2, 2)
        val insertedRepo3 = Repo(3, "repo3", "repo3", "repo3", insertedOwner, 3, 3, 3, 3)
        localDataSource.insert(insertedRepo)
        localDataSource.insert(insertedRepo2)
        localDataSource.insert(insertedRepo3)

        // When deleting all repos
        localDataSource.deleteAll()

        // And observe repos by owner login
        val observedUser = localDataSource.observeRepos(insertedOwner.login).getOrAwaitValue()

        // Then observed repos is empty list
        assertThat(observedUser, `is`(emptyList()))
    }
}