package com.example.news_solution.interfaces

import com.example.news_solution.models.Article

interface NewsService {
    suspend fun searchNews(searchQuery: String, pageNumber: Int)
    fun getAllArticle() : List<Article>
    suspend fun savedArticle(article : Article) : Long
    fun deleteArticle(article: Article)
}