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
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
@ExperimentalCoroutinesApi
class DefaultUserRepositoryTest {

    private val user1 = createNewUser(1, "laza")
    private val user2 = createNewUser(2, "laza2")
    private val user3 = createNewUser(3, "laza3")
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
    fun fetchUsers_requestUsersByNameFromRemoteDataSource() {
        // Given - Users login
        val login = "la"
        val expectedResult = fetchUsers(login, remoteUsers)

        // When fetching users by login
        val response = usersRepository.fetchUsers(login)
        val responseIsSuccessful = response.isSuccessful
        val fetchedUsers = response.body()?.items ?: emptyList()

        // Then - response is successful
        // and fetched users should be equal to expected result
        assertThat(responseIsSuccessful, `is` (true))
        assertThat(fetchedUsers, IsEqual(expectedResult))
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
    fun observeUsersByLogin_checkIfUserIsInDatabase() {
        // Given - users is already in fake db
        val expectedUser = user2

        // When - observe users by login
        val observedUsers = usersRepository.observeUsersByLogin(expectedUser.login).getOrAwaitValue()

        // Then - observed users should contain expected user
        assertThat(observedUsers, not(emptyList()))
        assertThat(observedUsers.size, `is` (1))
        assertThat(observedUsers.contains(expectedUser), `is`(true))
    }

    @Test
    fun insertOrReplace_insertNewUserAndCheckIfUserIsInDatabase() = mainCoroutineRule.runBlockingTest {
        // Given - new user
        val newUser = createNewUser(4, "new_user")

        // When - inserted in db and observed db
        usersRepository.insertOrReplace(newUser)
        val observedUsers = usersRepository.observeUsersByLogin(newUser.login).getOrAwaitValue()

        // Then - observed users should contain new user
        assertThat(observedUsers, not(emptyList()))
        assertThat(observedUsers.size, `is` (1))
        assertThat(observedUsers.contains(newUser), `is`(true))
    }

    @Test
    fun insertOrReplace_insertTwoNewUsersAndCheckIfUsersAreInDatabase() = mainCoroutineRule.runBlockingTest {
        // Given - two new users
        val firstNewUser = createNewUser(4, "new_user_one")
        val secondNewUser = createNewUser(5, "new_user_two")
        val bothUser: List<User> = listOf(firstNewUser, secondNewUser)

        // When - inserted in db and observed db
        usersRepository.insertOrReplace(firstNewUser, secondNewUser)
        val observedUsers = usersRepository.observeUsersByLogin("new_user").getOrAwaitValue()

        // Then - observed users is list of both users
        assertThat(observedUsers, not(emptyList()))
        assertThat(observedUsers.size, `is` (2))
        assertThat(observedUsers, `is`(bothUser))
    }

    @Test
    fun insertOrReplace_insertSameUserWithUpdatedValuesAndCheckIfReplacedUserIsInDatabase() = mainCoroutineRule.runBlockingTest {
        // Given - updated user
        val replacedUser = createNewUser(user2.id, user2.login, "new_user")

        // When - inserted in db and observed db
        usersRepository.insertOrReplace(replacedUser)
        val observedUsers = usersRepository.observeUsersByLogin(replacedUser.login).getOrAwaitValue()

        // Then - observed users should contain replaced user
        assertThat(observedUsers, not(emptyList()))
        assertThat(observedUsers.size, `is` (1))
        assertThat(observedUsers.contains(replacedUser), `is`(true))
    }



    @Test
    fun insertOrIgnore_insertSameUserWithUpdatedValuesAndCheckIfReplacedUserIsInDatabase() = mainCoroutineRule.runBlockingTest {
        // Given - updated user
        val replacedUser = createNewUser(user2.id, user2.login, "new_user")

        // When - inserted in db and observed db
        usersRepository.insertOrIgnore(replacedUser)
        val observedUsers = usersRepository.observeUsersByLogin(replacedUser.login).getOrAwaitValue()

        // Then - observed users should not contain replaced user
        assertThat(observedUsers, not(emptyList()))
        assertThat(observedUsers.size, `is` (1))
        assertThat(observedUsers.contains(replacedUser), not(true))
    }

    @Suppress("SameParameterValue")
    private fun fetchUsers(login: String, users: List<User>): List<User> {
        val listUsers = ArrayList<User>()

        for (user in users) {
            if (user.login.contains(login)) {
                listUsers.add(user)
            }
        }

        return listUsers
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

    private fun createNewUser(id: Int, rest: String): User {
        return createNewUser(id, rest, rest)
    }

    private fun createNewUser(id: Int, login: String, rest: String): User {
        return User(id, login, rest, rest, rest, rest, rest, rest, rest, rest)
    }
}