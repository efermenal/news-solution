package com.example.news_solution.db


import androidx.room.*

import com.example.news_solution.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun upsert(article: Article) : Long

      @Query("SELECT * FROM articles")
      fun getAllArticle() : Flow<List<Article>>

      @Delete
      suspend fun deleteArticle(article: Article)

}