package com.zhussain.githubzakcomponent.repository

import android.os.CountDownTimer
import androidx.lifecycle.LiveData

import com.zhussain.githubzakcomponent.AppExecutors
import com.zhussain.githubzakcomponent.api.ApiResponse
import com.zhussain.githubzakcomponent.api.GithubService
import com.zhussain.githubzakcomponent.dataclass.User
import com.zhussain.githubzakcomponent.db.UserDao
import com.zhussain.githubzakcomponent.util.Resource
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val githubService: GithubService
) {


    fun loadUser(login : String) : LiveData<Resource<User>> {
           return object : NetworkBoundResource<User,User>(appExecutors){
            override fun saveCallResult(item: User) {
                userDao.insert(user = item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<User> {
                return userDao.findByLogIn(login)
            }

            override fun createCall(): LiveData<ApiResponse<User>> {
             return githubService.getUser(login)
            }

        }.asLiveData()
    }


}