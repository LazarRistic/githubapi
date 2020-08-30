package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.overswayit.githubapi.entity.User

/**
 * Data Access Object for the users table.
 */
@Dao
interface UserDao {

    /**
     * Observes user with given login
     *
     * @param login the users login.
     * @return the user with login = [login].
     */
    @Query("SELECT * FROM users WHERE login = :login")
    fun observeUser(login: String): LiveData<User>

    /**
     * Insert a user/users in the database. If the user already exists, replace it.
     *
     * @param users the user/users to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg users: User)

    /**
     * Delete a user/users
     */
    @Delete
    suspend fun delete(vararg user: User)

    /**
     * Delete all users.
     */
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}