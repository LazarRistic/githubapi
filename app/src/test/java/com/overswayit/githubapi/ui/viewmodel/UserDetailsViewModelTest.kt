package com.overswayit.githubapi.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.overswayit.githubapi.MainCoroutineRule
import com.overswayit.githubapi.db.FakeUsersRepository
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.*
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserDetailsViewModelTest {

    // Subject under test
    private lateinit var viewModel: UserDetailsViewModel

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

        val user1 = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza", 1, 1, 1)

        runBlocking {
            usersRepository.addUser(user1)
        }

        viewModel = UserDetailsViewModel(usersRepository)
    }

    @Test
    fun `observe existing user with a given login`() {
        // Given a user login
        val login = "laza"

        // When observe user
        val observedUser = viewModel.observeUser(login).getOrAwaitValue()

        // Then observed user login should be same as login
        MatcherAssert.assertThat(observedUser, not(nullValue()))
        MatcherAssert.assertThat(observedUser.login, `is`(login))
    }

    @Test
    fun `observe non existing user with a given login`() {
        // Given a non existing user name
        val nonExistingLogin = "non_existing_user"

        // When observe user with non existing name
        val observedUser = viewModel.observeUser(nonExistingLogin).getOrAwaitValue()

        // Then observed user is not non existing name
        MatcherAssert.assertThat(observedUser, `is` (nullValue()))
    }
}