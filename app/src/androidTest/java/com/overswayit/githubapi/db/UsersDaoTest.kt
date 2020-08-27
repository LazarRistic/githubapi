package com.overswayit.githubapi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.db.GitHubAPIDatabase
import com.overswayit.githubapi.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
    fun insertUserAndGetByName() = runBlockingTest {
        // Given Insert user
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        database.userDao().insert(user)

        // When Get the user by id from the database
        val loadedUsers = database.userDao().getUsersByName(user.name)
        val loaded = loadedUsers.getOrNull(0)

        // Then The loaded data contains the expected values.
        assertThat(loadedUsers.size, `is` (1))
        assertThat<User>(loaded as User, notNullValue())
        assertThat(loaded.id, `is` (user.id))
        assertThat(loaded.login, `is` (user.login))
        assertThat(loaded.avatarUri, `is` (user.avatarUri))
        assertThat(loaded.name, `is` (user.name))
        assertThat(loaded.company, `is` (user.company))
        assertThat(loaded.bio, `is` (user.bio))
        assertThat(loaded.email, `is` (user.email))
        assertThat(loaded.location, `is` (user.location))
        assertThat(loaded.blog, `is` (user.blog))
    }

    @Test
    fun insertUserAndObserveByName() = runBlockingTest {
        // Given Insert user
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        database.userDao().insert(user)

        // When Get the user by id from the database
        val observedUsers = database.userDao().observeUsersByName(user.name).getOrAwaitValue()
        val observed = observedUsers.getOrNull(0)

        // Then The loaded data contains the expected values.
        assertThat(observedUsers.size, `is` (1))
        assertThat<User>(observed as User, notNullValue())
        assertThat(observed.id, `is` (user.id))
        assertThat(observed.login, `is` (user.login))
        assertThat(observed.avatarUri, `is` (user.avatarUri))
        assertThat(observed.name, `is` (user.name))
        assertThat(observed.company, `is` (user.company))
        assertThat(observed.bio, `is` (user.bio))
        assertThat(observed.email, `is` (user.email))
        assertThat(observed.location, `is` (user.location))
        assertThat(observed.blog, `is` (user.blog))
    }

    @Test
    fun addOneUserAndDeleteThemAndThenGetByName() = runBlockingTest {
        // Given Insert user
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        database.userDao().insert(user)

        // When delete that user
        database.userDao().delete(user)

        // and get the user by id from the database
        val loaded = database.userDao().observeUsersByName(user.name).getOrAwaitValue()

        // Then the loaded data should be empty
        assertThat(loaded.isNullOrEmpty(), `is` (true))
    }

    @Test
    fun addTwoUsersAndDeleteFirstOneThenGetByName() = runBlockingTest {
        // Given Insert user
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2")
        database.userDao().insert(user, user2)

        // When delete that user
        database.userDao().delete(user)

        // and get the user by id from the database
        val loadedUsers = database.userDao().observeUsersByName("%${user2.name}%").getOrAwaitValue()
        val loaded = loadedUsers.getOrNull(0)

        // Then the loaded data should be first user
        assertThat(loadedUsers.size, `is`(1))
        assertThat(loadedUsers.contains(user2), `is`(true))
        assertThat(loadedUsers.contains(user), `is`(false))
        assertThat(loaded, `is` (notNullValue()))
        assertThat(loaded, `is` (user2))
    }

    @Test
    fun deleteAllUsersThenGetByName() = runBlockingTest {
        // Given Insert user
        val user = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2")
        database.userDao().insert(user, user2)

        // When delete that user
        database.userDao().deleteAll()

        // and get the user by id from the database
        val loaded = database.userDao().observeUsersByName("%${user2.name}%").getOrAwaitValue()

        // Then the loaded data should be first user
        assertThat(loaded.isNullOrEmpty(), `is` (true))
    }
}