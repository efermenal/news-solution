package com.example.news_solution


import com.example.news_solution.db.ArticleDao
import com.example.news_solution.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow


class FakeLocalDataSource (var db : MutableList<Article>  = mutableListOf()): ArticleDao {
    override suspend fun upsert(article: Article): Long {
        db.add(article)
        return db.indexOf(article).toLong()
    }

    override fun getAllArticle(): Flow<List<Article>> {
       val list : List<MutableList<Article>> = listOf(db)
        return  list.asFlow()
    }

    override suspend fun deleteArticle(article: Article) {
        db.remove(article)
    }
}