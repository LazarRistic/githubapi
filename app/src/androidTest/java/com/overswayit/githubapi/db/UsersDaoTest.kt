package com.overswayit.githubapi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.overswayit.githubapi.entity.User
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
class UsersDaoTest {

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: GitHubAPIDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            GitHubAPIDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertUserAndObserveByName() = runBlockingTest {
        // Given Insert user
        val insertedUser = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1, 1, 1)
        database.userDao().insert(insertedUser).blockingAwait()

        // When Get the user by id from the database
        val loadedUser = database.userDao().observeUser(insertedUser.login).test()

        // Then The loaded user is inserted user
        loadedUser.assertValue(insertedUser)
    }

    @Test
    fun addOneUserAndDeleteThemAndThenObserveUser() = runBlockingTest {
        // Given Insert user
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1, 1, 1)
        database.userDao().insert(user).blockingAwait()

        // When delete that user
        database.userDao().delete(user).blockingAwait()

        // and get the user by id from the database
        val loaded = database.userDao().observeUser(user.login).test()

        // Then the loaded data should be empty
        loaded.assertEmpty()
    }

    @Test
    fun addTwoUsersAndDeleteFirstOneThenGetByName() = runBlockingTest {
        // Given Insert users
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1, 1, 1)
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", 1, 1, 1)
        database.userDao().insert(user, user2).blockingAwait()

        // When delete that user
        database.userDao().delete(user).blockingAwait()

        // and get the user2 by login from the database
        val loadedUser = database.userDao().observeUser(user2.login).test()

        // Then the loaded user is user2
        loadedUser.assertValue(user2)
    }

    @Test
    fun deleteAllUsersThenObserveUser() = runBlockingTest {
        // Given Insert users
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1, 1, 1)
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", 2, 2, 2)
        database.userDao().insert(user, user2).blockingAwait()

        // When delete that user
        database.userDao().deleteAll().blockingAwait()

        // and get the user2 by login from the database
        val loaded = database.userDao().observeUser(user2.login).test()

        // Then the loaded user is null
        loaded.assertEmpty()
    }
}