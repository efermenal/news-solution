package com.example.news_solution.di

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news_solution.MainActivity
import com.example.news_solution.di.viewmodels.ViewModelKey
import com.example.news_solution.interfaces.NewsService
import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.repositories.RemoteRepositoryImpl
import com.example.news_solution.services.Service
import com.example.news_solution.ui.home.HomeFragment
import com.example.news_solution.ui.home.HomeViewModel
import com.fernandocejas.sample.core.di.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

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
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsMovieDetailsViewModel(homeViewModel: HomeViewModel): ViewModel

}