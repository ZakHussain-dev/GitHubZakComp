package com.zhussain.githubzakcomponent.dataclass

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.zhussain.githubzakcomponent.db.GithubTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(GithubTypeConverters::class)
data class RepoSearchResult(
    val query: String,
    val repoIds: List<Int>,
    val totalCount: Int,
    val next: Int?
)