package com.example.news_solution.repositories

import com.example.news_solution.api.api
import com.example.news_solution.interfaces.NetworkInformation
import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


class RemoteRepositoryImpl
@Inject
constructor(
        private  val retrofit: Retrofit,
        private val networkState : NetworkInformation
) : RemoteRepository {
    private val remote by lazy { retrofit.create(api::class.java) }
    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Resource<NewsResponse> {
        if(networkState.isOnline()) {
            val response = remote.getBreakingNews(countryCode, pageNumber)
            return handleBreakingNews(response)
        }
        return Resource.Error("No internet Connection")
    }
    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Resource<NewsResponse> {
        if(networkState.isOnline()) {
            val response = remote.searchForNews(searchQuery, pageNumber)
            return handleBreakingNews(response)
        }
        return Resource.Error("No internet Connection")
    }

    private fun handleBreakingNews(response : Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}