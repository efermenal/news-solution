package com.example.news_solution.di

import android.annotation.SuppressLint
import com.example.news_solution.MainActivity
import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.repositories.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    @ActivityScope
    public abstract fun contributeMainActivity() : MainActivity

    @Binds
    @ActivityScope
    abstract  fun providesStorage(storage : RemoteRepositoryImpl) : RemoteRepository
}