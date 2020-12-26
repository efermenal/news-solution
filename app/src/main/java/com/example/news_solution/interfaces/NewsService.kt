package com.example.news_solution.interfaces

import androidx.lifecycle.LiveData
import com.example.news_solution.models.Article
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource

interface NewsService {
    suspend fun searchNews(searchQuery: String, pageNumber: Int): Resource<NewsResponse>
    suspend fun getBreakingNew(countryCode: String, pageNumber: Int): Resource<NewsResponse>
    fun getAllArticle() : LiveData<List<Article>>
    suspend fun savedArticle(article : Article) : Long
    suspend fun deleteArticle(article: Article)
}