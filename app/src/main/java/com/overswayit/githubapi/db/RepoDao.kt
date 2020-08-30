package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.overswayit.githubapi.entity.Repo

@Dao
interface RepoDao {

    /**
     * Observes repo with given owner login and repos name
     *
     * @param ownerLogin the owner login.
     * @param name the repos name.
     * @return the repo with owners login = [ownerLogin] and repos name = [name].
     */
    @Query("SELECT * FROM Repo WHERE owner_login = :ownerLogin AND name = :name")
    fun observeRepoByLoginAndName(ownerLogin: String, name: String): LiveData<Repo>

    /**
     * Observes repos with given owners login
     *
     * @param ownerLogin the owners login.
     * @return the repos with owners login = [ownerLogin].
     */
    @Query(
        """
        SELECT * FROM Repo
        WHERE owner_login = :ownerLogin
        ORDER BY stargazers_count DESC"""
    )
    fun observeRepositoriesByLogin(ownerLogin: String): LiveData<List<Repo>>

    /**
     * Insert a repo/repos in the database. If the repo already exists, replace it.
     *
     * @param repos the repo/repos to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg repos: Repo)

    /**
     * Delete all repos.
     */
    @Query("DELETE FROM Repo")
    suspend fun deleteAll()
}