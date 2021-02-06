package com.example.news_solution.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.news_solution.models.Article
import com.example.news_solution.models.Source
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {

    private lateinit var db : NewsDatabase
    private lateinit var articleDao: ArticleDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb(){
        val context : Context = ApplicationProvider.getApplicationContext()
        db = Room
                .inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        articleDao = db.getArticleDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown(){
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun upsert_insertAndGetArticle_returnsArticleInserted() = runBlockingTest {
        val article =  Article(
                id = 1,
                author = "Endherson Fermenal",
                content = "this is the content",
                description = "super description",
                publishedAt = "05/01/2020",
                source = Source("",""),
                title = "The insertAndGetArticle test",
                url = "www.image.com",
                urlToImage = "www.image.com/test.jpg",
        )
        articleDao.upsert(article)

        val articles = articleDao.getAllArticle().first()

        assertThat(articles[0], equalTo(article));
    }

    @Test
    @Throws(IOException::class)
    fun upsert_insertAndUpdateArticle_returnsArticleUpdated() = runBlockingTest {
        val article =  Article(
                id = 1,
                author = "Endherson Fermenal",
                content = "this is the content",
                description = "super description",
                publishedAt = "05/01/2020",
                source = Source("",""),
                title = "The insertAndGetArticle test",
                url = "www.image.com",
                urlToImage = "www.image.com/test.jpg",
        )

        articleDao.upsert(article)

        val newArticle = article.copy(author = "newAuthor", content = "newContent", title = "newTitle")
        articleDao.upsert(newArticle)

        val articles = articleDao.getAllArticle().first()

        assertThat(articles[0], equalTo(newArticle));
    }

    @Test
    @Throws(IOException::class)
    fun deleteArticle_insertAndDeleteArticle_returnsEmpty() = runBlockingTest {
        val article =  Article(
                id = 1,
                author = "Endherson Fermenal",
                content = "this is the content",
                description = "super description",
                publishedAt = "05/01/2020",
                source = Source("",""),
                title = "The insertAndGetArticle test",
                url = "www.image.com",
                urlToImage = "www.image.com/test.jpg",
        )

        articleDao.upsert(article)
        articleDao.deleteArticle(article)

        val articles = articleDao.getAllArticle().first()

        assertThat("article was not deleted", articles.isEmpty());

    }



}