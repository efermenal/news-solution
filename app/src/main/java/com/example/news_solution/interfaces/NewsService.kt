package com.example.news_solution.interfaces

import com.example.news_solution.models.Article
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource

interface NewsService {
    suspend fun searchNews(searchQuery: String, pageNumber: Int): Resource<NewsResponse>
    suspend fun getBreakingNew(countryCode: String, pageNumber: Int): Resource<NewsResponse>
    fun getAllArticle() : List<Article>
    suspend fun savedArticle(article : Article) : Long
    fun deleteArticle(article: Article)
}