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
import com.example.news_solution.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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
        title = "testing",
        author = "endherson fermenal",
        content = "content",
        description = "this is a description",
        publishedAt = "03/01/2021",
        source = null,
        url = "",
        urlToImage = ""
    )
    private val newsResponse = NewsResponse(
        articles = mutableListOf(article, article.copy(id=2)),
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
    fun getBreakingNews_retrieveAllNews_newsRetrieved() = runBlockingTest {
        val news = repository.getBreakingNew("",1)

        assertThat(news.data?.articles?.size, equalTo(2))
    }

    @Test
    fun getAllArticle_getArticleLocalDataSourceWhenDataIsEmpty_emptyRetrieved() = runBlockingTest {
        val articleInserted = repository.getAllArticle().getOrAwaitValue()

        assertThat(articleInserted.count(), equalTo(0))
    }

    @Test
    fun searchNews_getNewsRemoteDataSource_articlesMatched() = runBlockingTest {
        val searchResult = repository.searchNews("test", 1)

        assertThat(searchResult.data?.articles?.count(), equalTo(2))
    }

    @Test
    fun searchNews_getNewsRemoteDataSource() = runBlockingTest {
        val searchResult = repository.searchNews("x", 1)

        assertThat( searchResult, instanceOf(Resource.Error::class.java))
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