package com.zhussain.githubzakcomponent.db

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zhussain.githubzakcomponent.dataclass.Contributor
import com.zhussain.githubzakcomponent.dataclass.Repo
import com.zhussain.githubzakcomponent.dataclass.RepoSearchResult
import com.zhussain.githubzakcomponent.dataclass.User

/**
 * Interface for database access on Repo related operations.
 */

@Dao
abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(repo: Repo) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContributors(contributors: List<Contributor>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repos: List<Repo>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createRepoIfNotExists(repos: Repo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(repoSearchResult: RepoSearchResult)

    @Query("select * from repo where owner_login = :ownerLogin and name = :name")
    abstract fun load(ownerLogin: String, name: String): LiveData<Repo>

    @Query(
        """
        select login, contributions, avatarUrl,repoName,repoOwner from contributor
        where repoOwner = :owner AND repoName = :name order by contributions desc
    """
    )
    abstract fun loadContributors(owner: String, name: String): LiveData<List<Contributor>>

    @Query(
        """
        select * from repo where owner_login = :owner order by stars desc
    """
    )
    abstract fun loadRepositories(owner: String): LiveData<List<Repo>>

    @Query(
        """
        select * from reposearchresult where `query` = :query
    """
    )
    abstract fun search(query: String): LiveData<RepoSearchResult?>

    fun loadOrdered(repoIds: List<Int>): LiveData<List<Repo>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return loadById(repoIds).map { repositories ->
            repositories.sortedWith(compareBy { order.get(it.id) })
        }
    }

    @Query(
        """
        SELECT * FROM Repo WHERE id in (:repoIds)
    """
    )
    protected abstract fun loadById(repoIds: List<Int>): LiveData<List<Repo>>

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    abstract fun findSearchResult(query: String): RepoSearchResult?
}