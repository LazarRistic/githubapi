package com.overswayit.githubapi.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.overswayit.githubapi.MainCoroutineRule
import com.overswayit.githubapi.db.FakeUsersRepository
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchUsersViewModelTest {

    // Subject under test
    private lateinit var usersViewModel: SearchUsersViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var usersRepository: FakeUsersRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        // Initialising the users to 3
        usersRepository = FakeUsersRepository()

        val user1 = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2")
        val user3 = User(3, "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3")

        runBlocking {
            usersRepository.addUser(user1)
            usersRepository.addUser(user2)
            usersRepository.addUser(user3)
        }

        usersViewModel = SearchUsersViewModel(usersRepository)
    }

    @Test
    fun `observe existing user with a given name`() {
        // Given a user
        val user = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2")

        // When observe users with name same as given user
        usersViewModel.setQuery(user.login)
        val observedUsers = usersViewModel.observeUsersByName().getOrAwaitValue()
        val observedUser = observedUsers.getOrNull(0)

        // Then observed user should be same as user
        assertThat(observedUsers, `is` (not(emptyList())))
        assertThat(observedUsers.size, `is` (1))
        assertThat(observedUser, `is` (user))
    }



    @Test
    fun `observe non existing user with a given name`() {
        // Given a non existing user name
        val nonExistingName = "non_existing_user"

        // When observe users with non existing name
        usersViewModel.setQuery(nonExistingName)
        val observedUsers = usersViewModel.observeUsersByName().getOrAwaitValue()
        val observedUser = observedUsers.getOrNull(0)

        // Then observed user should be same as user
        assertThat(observedUsers, `is` (emptyList()))
        assertThat(observedUsers.size, `is` (0))
        assertThat(observedUser, `is` (nullValue()))
    }
}