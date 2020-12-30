package com.example.news_solution

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news_solution.interfaces.NewsService
import com.example.news_solution.models.Article
import com.example.news_solution.models.NewsResponse
import com.example.news_solution.utils.Resource
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NewsViewModel
@Inject
constructor(private  val service: NewsService) : ViewModel()
{

    private  var numberPageBreakingNews = 1
    var numberPageSearchNews = 1
    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews : LiveData<Resource<NewsResponse>>
        get() =_breakingNews

    private  var _searchNewsResponse : NewsResponse? = null
    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews : LiveData<Resource<NewsResponse>>
        get() =_searchNews




    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(code: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = service.getBreakingNew(code, numberPageBreakingNews)
        _breakingNews.postValue(response)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        Timber.d(article.toString())
        service.savedArticle(article)
    }

    fun getArticlesSaved() = service.getAllArticle()

    fun getSearchedNews (q : String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = service.searchNews(q, numberPageSearchNews)
        if (response is Resource.Success){
            Timber.d("SEARCH NEWS WAS OK. NUMBER PAGE %s WORD %s", numberPageSearchNews, q)
            numberPageSearchNews++
            if (_searchNewsResponse == null){
                _searchNewsResponse = response.data
            }else{
                val oldData = _searchNewsResponse?.articles
                val newData = response.data?.articles as List<Article>
                Timber.d("Old data %s", oldData?.size)
                Timber.d("New data %s", newData.size)
                oldData?.addAll(newData)
            }
        }
        _searchNews.postValue(if (_searchNewsResponse == null) response else Resource.Success(_searchNewsResponse) as Resource<NewsResponse>?)

    }

    fun deleteNew(article: Article) = viewModelScope.launch {
        service.deleteArticle(article)
    }



}