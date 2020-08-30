package com.overswayit.githubapi.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.overswayit.githubapi.MainCoroutineRule
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.getOrAwaitValue
import com.overswayit.githubapi.repository.FakeReposRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReposViewModelTest {

    // Subject under test
    private lateinit var viewModel: ReposViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var reposRepository: FakeReposRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        // Initialising the users to 3
        reposRepository = FakeReposRepository()

        val owner = Repo.Owner("login", "owner")
        val repo = Repo(5, "name", "repos", "repos", owner, 5, 5, 5, 5)

        runBlocking {
            reposRepository.addRepo(repo)
        }

        viewModel = ReposViewModel(reposRepository)
    }

    @Test
    fun `observe existing repos with a give owner login`() {
        // Given a owner login
        val login = "login"

        // When observe repos
        val observedRepos = viewModel.observeRepos(login).getOrAwaitValue()

        // Then observed repos is not empty
        // and observed repos size is 1
        assertThat(observedRepos, not(emptyList()))
        assertThat(observedRepos.size, `is`(1))
    }

    @Test
    fun `observe non existing user with a given owner login`() {
        // Given a non existing owner login
        val nonExistingLogin = "non_existing_login"

        // When observe repos with non existing owner login
        val observedRepos = viewModel.observeRepos(nonExistingLogin).getOrAwaitValue()

        // Then observed repos is empty list
        MatcherAssert.assertThat(observedRepos, `is` (emptyList()))
    }
}