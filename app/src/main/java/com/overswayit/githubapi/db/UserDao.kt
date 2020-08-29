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
     * Get list of users with given name LIKE.
     *
     * @param name the users name.
     * @return the list of users with name LIKE.
     */
    @Query("SELECT * FROM users WHERE login LIKE :name ORDER BY login DESC")
    suspend fun getUsersByName(name: String): List<User>

    /**
     * Observes list of users with given name LIKE.
     *
     * @param name the users name.
     * @return the list of users with name LIKE.
     */
    @Query("SELECT * FROM users WHERE login LIKE :name ORDER BY login DESC")
    fun observeUsersByName(name: String): LiveData<List<User>>

    /**
     * Insert a user/users in the database. If the user already exists, replace it.
     *
     * @param users the user/users to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(vararg users: User)

    /**
     * Insert a user/users in the database. If the user already exists, ignore it.
     *
     * @param users the user/users to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(vararg users: User)

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