package com.zhussain.githubzakcomponent.ui.user

import androidx.lifecycle.*
import com.zhussain.githubzakcomponent.dataclass.Repo
import com.zhussain.githubzakcomponent.dataclass.User
import com.zhussain.githubzakcomponent.repository.RepoRepository
import com.zhussain.githubzakcomponent.repository.UserRepository
import com.zhussain.githubzakcomponent.util.AbsentLiveData
import com.zhussain.githubzakcomponent.util.Resource
import javax.inject.Inject

/**
 *This fragment displays a user and their repositories.
 */
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repository: RepoRepository
) : ViewModel() {

    private val _login = MutableLiveData<String?>()
    val login: LiveData<String?>
        get() = _login

    val user: LiveData<Resource<User>> = _login.switchMap {
        if (it == null) {
            AbsentLiveData.create()
        } else {
            userRepository.loadUser(it)
        }
    }

    val repositories : LiveData<Resource<List<Repo>>> = _login.switchMap {
        if (it == null){
            AbsentLiveData.create()
        }else{
            repository.loadRepos(it)
        }
    }

    fun setLogin(login: String?) {
        if (_login.value != login) {
            _login.value = login
        }
    }

    fun retry() {
        _login.value?.let {
            _login.value = it;
        }
    }
}
