package com.zhussain.githubzakcomponent.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhussain.githubzakcomponent.ui.repo.RepoViewModel
import com.zhussain.githubzakcomponent.ui.search.SearchViewModel
import com.zhussain.githubzakcomponent.ui.user.UserViewModel
import com.zhussain.githubzakcomponent.viewmodel.GithubViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepoViewModel::class)
    abstract fun bindRepoViewModel(repoViewModel: RepoViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel) : ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory : GithubViewModelFactory) : ViewModelProvider.Factory
}