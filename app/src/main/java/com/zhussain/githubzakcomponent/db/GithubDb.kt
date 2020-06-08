package com.zhussain.githubzakcomponent.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zhussain.githubzakcomponent.dataclass.Contributor
import com.zhussain.githubzakcomponent.dataclass.Repo
import com.zhussain.githubzakcomponent.dataclass.RepoSearchResult
import com.zhussain.githubzakcomponent.dataclass.User
import javax.inject.Inject
import javax.inject.Singleton

@Database(
    entities = [
        User::class,
        Repo::class,
        Contributor::class,
        RepoSearchResult::class],
    version = 3,
    exportSchema = false
)

abstract class GithubDb : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getRepoDao(): RepoDao
}