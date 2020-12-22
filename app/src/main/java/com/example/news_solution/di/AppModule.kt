package com.example.news_solution.di

import dagger.Module
import dagger.Provides

@Module
object AppModule {
    @JvmStatic
    @Provides
    fun providesA(): String = "TESTING DAGGER. IT'S OK"
}