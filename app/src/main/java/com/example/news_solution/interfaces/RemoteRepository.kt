package com.example.news_solution.interfaces
import com.example.news_solution.Resource
import com.example.news_solution.models.NewsResponse

interface RemoteRepository {
 suspend fun  getBreakingNews(countryCode : String, pageNumber : Int) : Resource<NewsResponse>
 suspend fun searchNews(searchQuery: String, pageNumber: Int) : Resource<NewsResponse>
}