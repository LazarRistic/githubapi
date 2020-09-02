package com.overswayit.githubapi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.overswayit.githubapi.entity.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.function.Predicate

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class DefaultUsersLocalDataSourceTest {

    private lateinit var localDataSource: DefaultUsersLocalDataSource
    private lateinit var database: GitHubAPIDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            GitHubAPIDatabase::class.java
        ).allowMainThreadQueries().build()
        localDataSource = DefaultUsersLocalDataSource(database.userDao())
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insert_observeUser() {
        // Given a new user saved in database
        val user = User(
            1,
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            1,
            1,
            1
        )
        localDataSource.insert(user).blockingAwait()

        // When observe user by login
        val loaded = localDataSource.observeUser("laza").test()

        // Then same user is returned
        loaded.assertValue(user)
    }

    @Test
    fun delete_observeUser_resultIsNullValue() {
        // Given a new user saved in database
        val user = User(
            1,
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            1,
            1,
            1
        )
        localDataSource.insert(user).blockingAwait()

        // When deleting the same user
        localDataSource.delete(user).blockingAwait()

        // And observe user by name
        val observedUser = localDataSource.observeUser("laza").test()

        // Then observed user is null value
        observedUser.assertEmpty()

    }

    @Test
    fun deleteAll_getUser_resultIsNullValue() {
        // Given three new users in a database
        val user = User(
            1,
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            "laza",
            1,
            1,
            1
        )
        val user2 = User(
            2,
            "laza2",
            "laza2",
            "laza2",
            "laza2",
            "laza2",
            "laza2",
            "laza2",
            "laza2",
            "laza2",
            2,
            2,
            2
        )
        val user3 = User(
            3,
            "laza3",
            "laza3",
            "laza3",
            "laza3",
            "laza3",
            "laza3",
            "laza3",
            "laza3",
            "laza3",
            3,
            3,
            3
        )
        localDataSource.insert(user, user2, user3).blockingAwait()

        // When deleting all users
        localDataSource.deleteAll().blockingAwait()

        // And observe user by name
        val observedUser = localDataSource.observeUser("laza").test()

        // Then observed user is null value
        observedUser.assertEmpty()
    }
}
