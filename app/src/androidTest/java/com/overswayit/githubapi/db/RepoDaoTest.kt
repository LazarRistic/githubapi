package com.overswayit.githubapi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RepoDaoTest {

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: GitHubAPIDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GitHubAPIDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertRepoAndObserveRepositoriesByLogin() = runBlockingTest {
        // Given Insert repo
        val insertedOwner = Repo.Owner("login", "laza")
        val insertedRepo = Repo(1, "repo", "repo", "repo", insertedOwner, 1, 1, 1, 1)
        database.repoDao().insert(insertedRepo)

        // When observe repo by login from the database
        val loadedRepos =
            database.repoDao().observeRepositoriesByLogin(insertedOwner.login).getOrAwaitValue()
        val loadedRepo = loadedRepos[0]

        // Then loaded repos list is not empty
        // and loaded repos list size is 1
        // and The loaded repo is inserted repo
        assertThat(loadedRepos, not(emptyList()))
        assertThat(loadedRepos.size, `is`(1))
        assertThat(loadedRepo, `is`(loadedRepo))
    }

    @Test
    fun addTwoReposAndDeleteAllThenObserveRepositoriesByLogin() = runBlockingTest {
        // Given Insert repo
        val insertedOwner = Repo.Owner("login", "laza")
        val insertedRepo = Repo(1, "repo", "repo", "repo", insertedOwner, 1, 1, 1, 1)
        val insertedRepo2 = Repo(2, "repo2", "repo2", "repo2", insertedOwner, 2, 2, 2, 2)
        database.repoDao().insert(insertedRepo, insertedRepo2)

        // When delete all repos
        database.repoDao().deleteAll()

        // and observe repos by login from the database
        val observedRepos =
            database.repoDao().observeRepositoriesByLogin(insertedOwner.login).getOrAwaitValue()

        // Then the observed repos is empty list
        assertThat(observedRepos, `is`(emptyList()))
    }

    @Test
    fun addRepoAndObserveRepoByLoginAndName() = runBlockingTest {
        // Given Insert repo
        val insertedOwner = Repo.Owner("login", "laza")
        val insertedRepo = Repo(1, "repo", "repo", "repo", insertedOwner, 1, 1, 1, 1)
        database.repoDao().insert(insertedRepo)

        // When observed repo by owner login and repo name from database
        val observedRepo = database.repoDao().observeRepoByLoginAndName(insertedOwner.login, insertedRepo.name).getOrAwaitValue()

        // Then the observed repo is same as inserted repo
        assertThat(observedRepo, not(nullValue()))
        assertThat(observedRepo, `is`(insertedRepo))
    }
}