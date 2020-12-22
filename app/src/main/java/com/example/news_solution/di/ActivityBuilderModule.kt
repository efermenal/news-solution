package com.example.news_solution.di

import android.annotation.SuppressLint
import com.example.news_solution.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    public abstract fun contributeMainActivity() : MainActivity


}