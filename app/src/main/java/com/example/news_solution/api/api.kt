package com.example.news_solution.api

import com.example.news_solution.BuildConfig.API_KEY
import com.example.news_solution.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface api {

    @Headers("X-Api-Key: $API_KEY")
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode : String = "us",
        @Query("page")
        pageNumber : Int = 1
    ): Response<NewsResponse>

    @Headers("X-Api-Key: $API_KEY")
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery : String ,
        @Query("page")
        pageNumber : Int = 1
    ): Response<NewsResponse>

}