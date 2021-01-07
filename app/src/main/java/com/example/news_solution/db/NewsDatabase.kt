package com.example.news_solution.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.news_solution.models.Article


@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
/*Source isn't a primitive value without this cannot figure out how to save this field in database*/
@TypeConverters(Converters::class)
abstract class
NewsDatabase : RoomDatabase() {
    abstract fun getArticleDao() : ArticleDao
}