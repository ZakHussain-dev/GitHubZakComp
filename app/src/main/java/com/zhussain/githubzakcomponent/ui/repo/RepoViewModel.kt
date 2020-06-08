package com.zhussain.githubzakcomponent.ui.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.zhussain.githubzakcomponent.dataclass.Contributor
import com.zhussain.githubzakcomponent.dataclass.Repo
import com.zhussain.githubzakcomponent.repository.RepoRepository
import com.zhussain.githubzakcomponent.util.AbsentLiveData
import com.zhussain.githubzakcomponent.util.Resource
import javax.inject.Inject


class RepoViewModel @Inject constructor(repository: RepoRepository) : ViewModel() {

    private val _repoIds: MutableLiveData<RepoId> = MutableLiveData()

    val repoId: LiveData<RepoId> get() = _repoIds

    val repo: LiveData<Resource<Repo>> = _repoIds.switchMap { input ->
        input.ifExists { owner, name ->
            repository.loadRepo(owner, name)
        }
    }

    val contributors: LiveData<Resource<List<Contributor>>> = _repoIds.switchMap { input ->
        input.ifExists { owner, name ->
            repository.loadContributors(owner, name)
        }
    }

    fun setId(owner: String, name: String) {
        val update = RepoId(owner, name)
        if (_repoIds.value == update) {
            return
        }
        _repoIds.value = update
    }

    fun retry() {
        val owner = _repoIds.value?.owner
        val name = _repoIds.value?.name
        if (owner != null && name != null) {
            _repoIds.value = RepoId(owner, name)
        }
    }

    data class RepoId(val owner: String, val name: String) {
        fun <T> ifExists(f: (String, String) -> LiveData<T>): LiveData<T> {
            return if (owner.isBlank() || name.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(owner, name)
            }
        }
    }

}
