package com.overswayit.githubapi.repository

import com.overswayit.githubapi.entity.Commit

interface CommitsRepository {
    fun fetchCommits(ownerLogin: String, repo: String): List<Commit>
}