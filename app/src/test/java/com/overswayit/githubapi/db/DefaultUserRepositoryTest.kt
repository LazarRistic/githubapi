package com.overswayit.githubapi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.overswayit.githubapi.MainCoroutineRule
import com.overswayit.githubapi.api.FakeDefaultRemoteDataSource
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.getOrAwaitValue
import com.overswayit.githubapi.repository.DefaultUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
@ExperimentalCoroutinesApi
class DefaultUserRepositoryTest {

    private val user1 = createNewUser(1, "laza", 1)
    private val user2 = createNewUser(2, "laza2", 2)
    private val user3 = createNewUser(3, "laza3", 3)
    private val localUsers = listOf(user1, user2).sortedBy { it.id }
    private val remoteUsers = listOf(user3).sortedBy { it.id }
    private lateinit var usersRemoteDataSource: FakeDefaultRemoteDataSource
    private lateinit var usersLocalRemoteDataSource: FakeDefaultUsersLocalDataSource

    // Class under test
    private lateinit var usersRepository: DefaultUserRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        usersRemoteDataSource = FakeDefaultRemoteDataSource(ArrayList(remoteUsers))
        usersLocalRemoteDataSource = FakeDefaultUsersLocalDataSource(localUsers.toMutableList())

        // Get a reference to the class under test
        usersRepository = DefaultUserRepository(usersRemoteDataSource, usersLocalRemoteDataSource)
    }

    @Test
    fun fetchUser_requestUserInformationByLoginFromRemoteDataSource() {
        // Given - Users login
        val login = "laza3"
        val expectedResult = fetchUser(login, remoteUsers)

        // When - fetching user by login
        val response = usersRepository.fetchUser(login)
        val responseIsSuccessful = response.isSuccessful
        val fetchedUser = response.body()

        // Then - response should be successful
        // and fetched user should be equal to expected result
        assertThat(responseIsSuccessful, `is`(true))
        assertThat(fetchedUser, IsEqual(expectedResult))
    }

    @Test
    fun observeUser_checkIfUserIsInDatabase() {
        // Given - user is already in fake db
        val expectedUser = user2

        // When - observe user by login
        val observedUser = usersRepository.observeUser(expectedUser.login).getOrAwaitValue()

        // Then - observed user is expected user
        assertThat(observedUser, not(nullValue()))
        assertThat(observedUser, `is`(expectedUser))
    }

    @Test
    fun insert_insertNewUserAndCheckIfUserIsInDatabase() = mainCoroutineRule.runBlockingTest {
        // Given - new user
        val newUser = createNewUser(4, "new_user", 4)

        // When - inserted in db and observed db
        usersRepository.insert(newUser)
        val observedUser = usersRepository.observeUser(newUser.login).getOrAwaitValue()

        // Then - observed user is new user
        assertThat(observedUser, not(nullValue()))
        assertThat(observedUser, `is`(newUser))
    }

    @Test
    fun insertOrIgnore_insertSameUserWithUpdatedValuesAndCheckIfReplacedUserIsNotInDatabase() = mainCoroutineRule.runBlockingTest {
        // Given - updated user
        val replacedUser = createNewUser(user2.id, user2.login, "new_user", 8)

        // When - inserted in db and observed db
        usersRepository.insert(replacedUser)
        val observedUser = usersRepository.observeUser(replacedUser.login).getOrAwaitValue()

        // Then - observed user is not replaced user
        assertThat(observedUser, not(nullValue()))
        assertThat(observedUser, not(replacedUser))
    }

    @Suppress("SameParameterValue")
    private fun fetchUser(login: String, users: List<User>): User? {
        for (user in users) {
            if (user.login == login) {
                return user
            }
        }

        return null
    }

    private fun createNewUser(id: Int, rest: String, numbers: Int): User {
        return createNewUser(id, rest, rest, numbers)
    }

    private fun createNewUser(id: Int, login: String, rest: String, numbers: Int): User {
        return User(id, login, rest, rest, rest, rest, rest, rest, rest, rest, numbers, numbers, numbers)
    }
}