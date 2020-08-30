package com.overswayit.githubapi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(), GitHubAPIDatabase::class.java).allowMainThreadQueries().build()
        localDataSource = DefaultUsersLocalDataSource(database.userDao())
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insert_observeUser() = runBlocking {
        // Given a new user saved in database
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1, 1, 1)
        localDataSource.insert(user)

        // When observe user by login
        val loaded = localDataSource.observeUser("laza").getOrAwaitValue()

        // Then same user is returned
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.id, `is`(1))
        assertThat(loaded.login, `is`("laza"))
        assertThat(loaded.avatarUri, `is`("laza"))
        assertThat(loaded.name, `is`("laza"))
        assertThat(loaded.company, `is`("laza"))
        assertThat(loaded.bio, `is`("laza"))
        assertThat(loaded.email, `is`("laza"))
        assertThat(loaded.location, `is`("laza"))
        assertThat(loaded.blog, `is`("laza"))
    }

    @Test
    fun delete_observeUser_resultIsNullValue() = runBlocking {
        // Given a new user saved in database
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1, 1, 1)
        localDataSource.insert(user)

        // When deleting the same user
        localDataSource.delete(user)

        // And observe user by name
        val observedUser = localDataSource.observeUser("laza").getOrAwaitValue()

        // Then observed user is null value
        assertThat(observedUser, `is`(nullValue()))
    }

    @Test
    fun deleteAll_getUser_resultIsNullValue() = runBlocking {
        // Given three new users in a database
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1 ,1 ,1)
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", 2, 2, 2)
        val user3 = User(3, "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", 3, 3, 3)
        localDataSource.insert(user, user2, user3)

        // When deleting all users
        localDataSource.deleteAll()

        // And observe user by name
        val observedUser = localDataSource.observeUser("laza").getOrAwaitValue()

        // Then observed user is null value
        assertThat(observedUser, `is`(nullValue()))
    }
}
