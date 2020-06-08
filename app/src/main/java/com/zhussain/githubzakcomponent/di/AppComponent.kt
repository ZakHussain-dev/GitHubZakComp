package com.zhussain.githubzakcomponent.di

import android.content.Context
import com.zhussain.githubzakcomponent.MainActivity
import com.zhussain.githubzakcomponent.ui.repo.RepoFragment
import com.zhussain.githubzakcomponent.ui.search.SearchFragment
import com.zhussain.githubzakcomponent.ui.user.UserFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment : SearchFragment)
    fun inject(fragment : RepoFragment)
    fun inject(fragment : UserFragment)

}