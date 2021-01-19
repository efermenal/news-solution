package com.example.news_solution.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.news_solution.FakeLocalDataSource
import com.example.news_solution.FakeRemoteDataSource
import com.example.news_solution.db.ArticleDao
import com.example.news_solution.getOrAwaitValue
import com.example.news_solution.interfaces.NewsService
import com.example.news_solution.interfaces.RemoteRepository
import com.example.news_solution.models.Article
import com.example.news_solution.models.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class ServiceTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var fakeRemoteDataSource: RemoteRepository
    private lateinit var fakeLocalDataSource: ArticleDao
    private lateinit var repository: NewsService
    private val article = Article(
        id = 1,
        title = "Testing Test xD",
        author = "Endherson Fermenal",
        content = "content",
        description = "this is a description",
        publishedAt = "03/01/2021",
        source = null,
        url = "",
        urlToImage = ""
    )
    private val newsResponse = NewsResponse(
        articles = mutableListOf(),
        status = "OK",
        totalResults = 0
    )

    @Before
    fun createRepository(){
        fakeRemoteDataSource = FakeRemoteDataSource(newsResponse)
        fakeLocalDataSource = FakeLocalDataSource()
        repository = Service(fakeRemoteDataSource, fakeLocalDataSource, Dispatchers.Unconfined)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun getAllArticle_addAndGetArticleLocalDataSource_articleRetrieved() = runBlockingTest {
        repository.savedArticle(article)
        val articleInserted = repository.getAllArticle().getOrAwaitValue()

        assertThat(articleInserted[0], equalTo(article))
    }

    @Test
    fun savedArticle_addArticleLocalDataSource_articleSaved() = runBlockingTest {
        // Arrange phase is empty: initializing in createRepository
        // Act
        repository.savedArticle(article)
        // Assert
        assertThat("Not stored", (fakeLocalDataSource as FakeLocalDataSource).db.contains(article))
    }

    @Test
    fun deleteArticle_removeArticleLocalDataSource_articleRemoved() = runBlockingTest {
        repository.savedArticle(article)
        repository.deleteArticle(article)
        assertThat("Not empty", !(fakeLocalDataSource as FakeLocalDataSource).db.contains(article))
    }


}