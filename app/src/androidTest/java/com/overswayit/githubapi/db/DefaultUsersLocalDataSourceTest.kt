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
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue
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
    fun insert_getUser() = runBlocking {
        // Given a new user saved in database
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        localDataSource.insertOrIgnore(user)

        // When getting user by name
        val loadedUsers = localDataSource.getUsersByName("laza")
        val loaded = loadedUsers.getOrNull(0)

        // Then same user is returned
        assertThat(loadedUsers, `is`(not(emptyList())))
        assertThat(loadedUsers.size, `is`(1))
        assertThat(loadedUsers.contains(user), `is`(true))
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded!!.id, `is`(1))
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
    fun insert_observeUser() = runBlocking {
        // Given a new user saved in database
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        localDataSource.insertOrIgnore(user)

        // When getting user by name
        val loadedUsers = localDataSource.observeUsersByName("laza").getOrAwaitValue()
        val loaded = loadedUsers.getOrNull(0)

        // Then same user is returned
        assertThat(loadedUsers, `is`(not(emptyList())))
        assertThat(loadedUsers.size, `is`(1))
        assertThat(loadedUsers.contains(user), `is`(true))
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded!!.id, `is`(1))
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
    fun delete_getUser() = runBlocking {
        // Given a new user saved in database
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        localDataSource.insertOrIgnore(user)

        // When deleting the same user
        localDataSource.delete(user)

        // And getting user by name
        val loadedUsers = localDataSource.getUsersByName("laza")

        // Then same user is returned
        assertThat(loadedUsers, `is`(emptyList()))
        assertThat(loadedUsers.size, `is`(0))
        assertThat(loadedUsers.contains(user), `is`(false))
    }

    @Test
    fun deleteAll_getUser_resultShouldBeEmpty() = runBlocking {
        // Given three new users in a databse
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2")
        val user3 = User(3, "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3")
        localDataSource.insertOrIgnore(user, user2, user3)

        // When deleting all users
        localDataSource.deleteAll()

        // And getting users by name
        val loadedUsers = localDataSource.getUsersByName("%laza%")

        // Then same user is returned
        assertThat(loadedUsers, `is`(emptyList()))
        assertThat(loadedUsers.size, `is`(0))
        assertThat(loadedUsers.contains(user), `is`(false))
    }
}
