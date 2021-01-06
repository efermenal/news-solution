package com.example.news_solution

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_solution.db.ArticleDao
import com.example.news_solution.models.Article

class FakeLocalDataSource (var db : MutableList<Article>  = mutableListOf()): ArticleDao {
    override suspend fun upsert(article: Article): Long {
        db.add(article)
        return db.indexOf(article).toLong()
    }

    override fun getAllArticle(): LiveData<List<Article>> {
        val list = MutableLiveData<List<Article>>()
        list.value = db.toList()
        return list
    }

    override suspend fun deleteArticle(article: Article) {
        db.remove(article)
    }
}