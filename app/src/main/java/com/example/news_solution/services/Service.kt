package com.example.news_solution.services

import com.example.news_solution.di.ActivityScope
import com.example.news_solution.di.AppScope
import com.example.news_solution.interfaces.NewsService
import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.models.Article
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource
import javax.inject.Inject


class Service
@Inject
constructor(private val remoteRepository: RemoteRepository) : NewsService {
    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Resource<NewsResponse> {
        return remoteRepository.searchNews(searchQuery, pageNumber)
    }

    override suspend fun getBreakingNew(
        countryCode: String,
        pageNumber: Int
    ): Resource<NewsResponse> {
        return remoteRepository.getBreakingNews(countryCode, pageNumber)
    }


    override fun getAllArticle(): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun savedArticle(article: Article): Long {
        TODO("Not yet implemented")
    }

    override fun deleteArticle(article: Article) {
        TODO("Not yet implemented")
    }
}