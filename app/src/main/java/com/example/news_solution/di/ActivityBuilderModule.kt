package com.example.news_solution.di

import android.app.Application
import com.example.news_solution.MainActivity
import com.example.news_solution.interfaces.NetworkInformation
import com.example.news_solution.interfaces.NewsService
import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.repositories.RemoteRepositoryImpl
import com.example.news_solution.services.Service
import com.example.news_solution.ui.home.HomeFragment
import com.example.news_solution.utils.NetworkState
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    @ActivityScope
    public abstract fun contributeMainActivity() : MainActivity

    @ContributesAndroidInjector
    @ActivityScope
    abstract fun contributeHomeFragment() : HomeFragment

    @Binds
    //@ActivityScope
    abstract  fun bindStorage(storage : RemoteRepositoryImpl) : RemoteRepository

    @Binds
    //@ActivityScope
    abstract  fun bindService(service : Service) : NewsService

    @Binds
    abstract fun bindNetWorkState(networkState: NetworkState) : NetworkInformation


}