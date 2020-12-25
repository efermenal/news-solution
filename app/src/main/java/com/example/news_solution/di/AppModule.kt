package com.example.news_solution.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.news_solution.BuildConfig
import com.example.news_solution.db.NewsDatabase
import com.example.news_solution.utils.Constants.BASE_URL
import com.example.news_solution.utils.Constants.DATABASE_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
object AppModule {

    @Provides
    @AppScope
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun createClient() : OkHttpClient{
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }


    @Provides
    @AppScope
    fun providesNewsDatabase(application: Application)  =
        Room.databaseBuilder(
            application,
            NewsDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    @AppScope
    fun providesArticleDao(db : NewsDatabase) = db.getArticleDao()
}