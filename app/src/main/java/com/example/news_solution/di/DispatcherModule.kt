package com.example.news_solution.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object DispatcherModule {

    @IoDispatcher
    @Provides
    fun providesIoDispatcher() : CoroutineDispatcher = Dispatchers.IO
}