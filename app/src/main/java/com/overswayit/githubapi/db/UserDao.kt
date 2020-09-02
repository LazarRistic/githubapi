package com.overswayit.githubapi.db

import androidx.room.*
import com.overswayit.githubapi.entity.User
import io.reactivex.Completable
import io.reactivex.Observable

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
    fun observeUser(login: String): Observable<User>

    /**
     * Insert a user/users in the database. If the user already exists, replace it.
     *
     * @param users the user/users to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg users: User): Completable

    /**
     * Delete a user/users
     */
    @Delete
    fun delete(vararg user: User): Completable

    /**
     * Delete all users.
     */
    @Query("DELETE FROM users")
    fun deleteAll(): Completable
}