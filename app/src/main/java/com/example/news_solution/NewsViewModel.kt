package com.example.news_solution

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news_solution.interfaces.NewsService
import com.example.news_solution.models.Article
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel
@Inject
constructor(private  val service: NewsService) : ViewModel()
{
    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews : LiveData<Resource<NewsResponse>>
        get() =_breakingNews

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews : LiveData<Resource<NewsResponse>>
        get() =_searchNews


    val numberPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(code: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = service.getBreakingNew(code, numberPage)
        _breakingNews.postValue(response)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        service.savedArticle(article)
    }

    fun getArticlesSaved() = service.getAllArticle()

    fun getSearchedNews (q : String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = service.searchNews(q, numberPage)
        _searchNews.postValue(response)
    }

    fun deleteNew(article: Article) = viewModelScope.launch {
        service.deleteArticle(article)
    }



}