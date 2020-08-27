package com.overswayit.githubapi.db

import com.overswayit.githubapi.MainCoroutineRule
import com.overswayit.githubapi.api.FakeDefaultRemoteDataSource
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.DefaultUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
@ExperimentalCoroutinesApi
class DefaultUserRepositoryTest {

    private val user1 = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
    private val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2")
    private val user3 = User(3, "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3")
    private val localUsers = listOf(user1, user2).sortedBy { it.id }
    private val remoteUsers = listOf(user3).sortedBy { it.id }
    private lateinit var usersRemoteDataSource: FakeDefaultRemoteDataSource
    private lateinit var usersLocalRemoteDataSource: FakeDefaultUsersLocalDataSource

    // Class under test
    private lateinit var usersRepository: DefaultUserRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        usersRemoteDataSource = FakeDefaultRemoteDataSource()
        usersLocalRemoteDataSource = FakeDefaultUsersLocalDataSource(localUsers.toMutableList())

        // Get a reference to the class under test
        usersRepository = DefaultUserRepository(usersRemoteDataSource, usersLocalRemoteDataSource)
    }

    @Test
    fun getUsersByName_requestUsersByNameFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // When users are searched from user repository
        val users = usersRepository.getUsersByName("la")

        assertThat(users, IsEqual(localUsers))
    }
}