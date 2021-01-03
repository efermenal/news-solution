package com.example.news_solution.services

import androidx.lifecycle.LiveData
import com.example.news_solution.db.ArticleDao
import com.example.news_solution.di.ActivityScope
import com.example.news_solution.di.AppScope
import com.example.news_solution.di.IoDispatcher
import com.example.news_solution.interfaces.NewsService
import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.models.Article
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Service
@Inject
constructor(
    private val remoteRepository: RemoteRepository,
    private val localData : ArticleDao,
    @IoDispatcher private val ioDispatcher : CoroutineDispatcher
) : NewsService {

    override suspend fun searchNews(
        searchQuery: String,
        pageNumber: Int
    )  = withContext(ioDispatcher){remoteRepository.searchNews(searchQuery, pageNumber)}

    override suspend fun getBreakingNew(
        countryCode: String,
        pageNumber: Int
    ): Resource<NewsResponse> = withContext(ioDispatcher){ remoteRepository.getBreakingNews(countryCode, pageNumber)}


    override fun getAllArticle(): LiveData<List<Article>> = localData.getAllArticle()

    override suspend fun savedArticle(article: Article): Long = withContext(ioDispatcher){ localData.upsert(article)}

    override suspend fun deleteArticle(article: Article) = withContext(ioDispatcher){ localData.deleteArticle(article)}
}