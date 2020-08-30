package com.overswayit.githubapi.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.overswayit.githubapi.MainCoroutineRule
import com.overswayit.githubapi.api.FakeDefaultReposRemoteDataSource
import com.overswayit.githubapi.db.FakeDefaultReposLocalDataSource
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
class DefaultReposRepositoryTest {

    private val repo1 = createNewRepo(1, "laza", "laza", 1)
    private val repo2 = createNewRepo(2, "laza2", "laza2", 2)
    private val repo3 = createNewRepo(3, "laza3", "laza3", 3)
    private val localRepos = listOf(repo1, repo2).sortedBy { it.id }
    private val remoteRepos = listOf(repo3).sortedBy { it.id }
    private lateinit var reposRemoteRemoteDataSource: FakeDefaultReposRemoteDataSource
    private lateinit var reposLocalRemoteDataSource: FakeDefaultReposLocalDataSource

    // Class under test
    private lateinit var reposRepository: DefaultReposRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        reposRemoteRemoteDataSource = FakeDefaultReposRemoteDataSource(ArrayList(remoteRepos))
        reposLocalRemoteDataSource = FakeDefaultReposLocalDataSource(localRepos.toMutableList())

        // Get a reference to the class under test
        reposRepository = DefaultReposRepository(reposRemoteRemoteDataSource, reposLocalRemoteDataSource)
    }

    @Test
    fun fetchRepos_requestReposByOwnerLoginFromRemoteDataSource() {
        // Given - Repo Owner login
        val ownerLogin = "login"
        val expectedResult = remoteRepos

        // When - fetching repos by owner login
        val response = reposRepository.fetchRepos(ownerLogin)
        val responseIsSuccessful = response.isSuccessful
        val fetchedRepos = response.body()

        // Then - response is successful
        // and fetched repos is equal to expected result
        assertThat(responseIsSuccessful, `is`(true))
        assertThat(fetchedRepos, IsEqual(expectedResult))
    }

    @Test
    fun observeRepo_checkIfRepoIsInDatabase() {
        // Given - Existing Repo
        val existingRepo = fetchRepo(repo2.owner.login, repo2.name, localRepos)!!

        // When - Observe repo with owner login and name
        val observeRepo = reposRepository.observeRepo(existingRepo.owner.login, existingRepo.name).getOrAwaitValue()

        // Then - observed repo is same as existing repo
        assertThat(observeRepo, not(nullValue()))
        assertThat(observeRepo, `is`(existingRepo))
    }

    @Test
    fun observeRepos_checkIfReposAreInDatabase() {
        // Given - Existing Repo
        val existingRepo = fetchRepo(repo2.owner.login, repo2.name, localRepos)!!

        // When - Observe repos with owner login
        val observeRepos = reposRepository.observeRepos(existingRepo.owner.login).getOrAwaitValue()

        // Then - observed repos is not empty list
        // and observed repos size is 2
        // and observed repo contains existing repo
        assertThat(observeRepos, not(emptyList()))
        assertThat(observeRepos.size, `is`(2))
        assertThat(observeRepos.contains(existingRepo), `is`(true))
    }

    @Test
    fun insert_newRepoAndCheckIfRepoIsInDatabase() {
        // Given - inserted Repo
        val insertedRepo = createNewRepo(4, "laza4", "laza4", 4)
        runBlocking {
            reposRepository.insert(insertedRepo)
        }

        // When - Observe repo with inserted repo owner login and inserted repo name
        val observeRepo = reposRepository.observeRepo(insertedRepo.owner.login, insertedRepo.name).getOrAwaitValue()

        // Then - observed repo is same as inserted repo
        assertThat(observeRepo, not(nullValue()))
        assertThat(observeRepo, `is`(insertedRepo))
    }

    @Test
    fun deleteAll_checkIfThereIsNoReposInDatabase() {
        // Given - deleting all repos
        runBlocking {
            reposRepository.deleteAll()
        }

        // When - Observe repos with owner login
        val observeReposAfterDeleteAll = reposRepository.observeRepos(repo2.owner.login).getOrAwaitValue()

        // Then - observed repos after delete all is empty list
        assertThat(observeReposAfterDeleteAll, `is`(emptyList()))
    }

    private fun fetchRepo(ownerLogin: String, name: String, repos: List<Repo>): Repo? {
        for (repo in repos) {
            if (repo.owner.login == ownerLogin && repo.name == name) {
                return repo
            }
        }

        return null
    }

    private fun createNewRepo(id: Int, name: String, rest: String, numbers: Int): Repo {
        val owner = Repo.Owner("login", "owner")
        return Repo(id, name, rest, rest, owner, numbers, numbers, numbers, numbers)
    }
}