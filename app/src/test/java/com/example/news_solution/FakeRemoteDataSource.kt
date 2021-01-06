package com.example.news_solution

import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource

class FakeRemoteDataSource (var response : NewsResponse?) : RemoteRepository {


    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Resource<NewsResponse> {

        response?.let {
            return Resource.Success(it)
        }
        return Resource.Error("NO FOUND")
    }

    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Resource<NewsResponse> {
        response?.let {
            return Resource.Success(it)
        }
        return Resource.Error("NO RESULTS")
    }
}