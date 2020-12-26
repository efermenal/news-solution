package com.example.news_solution.services

import androidx.lifecycle.LiveData
import com.example.news_solution.db.ArticleDao
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
constructor(
    private val remoteRepository: RemoteRepository,
    private val localData : ArticleDao
) : NewsService {

    override suspend fun searchNews(
        searchQuery: String,
        pageNumber: Int
    ): Resource<NewsResponse> = remoteRepository.searchNews(searchQuery, pageNumber)

    override suspend fun getBreakingNew(
        countryCode: String,
        pageNumber: Int
    ): Resource<NewsResponse> =  remoteRepository.getBreakingNews(countryCode, pageNumber)


    override fun getAllArticle(): LiveData<List<Article>> = localData.getAllArticle()

    override suspend fun savedArticle(article: Article): Long = localData.upsert(article)

    override suspend fun deleteArticle(article: Article) = localData.deleteArticle(article)
}