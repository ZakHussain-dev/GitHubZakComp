package com.zhussain.githubzakcomponent.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zhussain.githubzakcomponent.dataclass.User

/**
 * Interface for database access for User related operations.
 */
@Dao
interface UserDao {
    @Query("select * from user where login = :login")
    fun findByLogIn(login: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User) : Long
}