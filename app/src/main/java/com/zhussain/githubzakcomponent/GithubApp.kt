package com.zhussain.githubzakcomponent

import android.app.Application
import com.zhussain.githubzakcomponent.di.AppComponent
import com.zhussain.githubzakcomponent.di.DaggerAppComponent

class GithubApp : Application() {

    val appComponent : AppComponent by lazy {
        initializeAppComponnt()
    }

    private fun initializeAppComponnt(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

}