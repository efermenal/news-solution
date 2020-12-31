package com.example.news_solution.ui.searchNews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news_solution.MainActivity
import com.example.news_solution.NewsViewModel
import com.example.news_solution.R
import com.example.news_solution.adapters.ArticlesAdapter
import com.example.news_solution.databinding.FragmentSearchNewsBinding
import com.example.news_solution.utils.Constants.BUNDLE_ARTICLE
import com.example.news_solution.utils.Constants.DELAY_BETWEEN_SEARCH
import com.example.news_solution.utils.Constants.SIZE_PAGE
import com.example.news_solution.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding: FragmentSearchNewsBinding
        get() = _binding!!

    private var isLoading = false
    private var isScrolling = false
    private var isLastPage = false

    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        settingObservers()
        settingListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        articlesAdapter = ArticlesAdapter()
        binding.rvSearchNews.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrolllistener)
        }
    }

    private fun settingObservers() {
        viewModel.searchNews.removeObservers(viewLifecycleOwner)
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->

            when(response){
                is Resource.Loading ->{
                    showProgressbar()
                }
                is Resource.Success -> {
                    hideProgressbar()
                    response.data?.let { newsResponse->
                        articlesAdapter.differ.submitList(newsResponse.articles.toList())
                        Timber.d("List sent: %s",newsResponse.articles.size)
                        val totalPages = newsResponse.totalResults / SIZE_PAGE + 2
                        isLastPage = viewModel.numberPageSearchNews == totalPages
                        if (isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressbar()
                    Timber.d("ERROR %s", response.message)
                }
            }


        })
    }

    private val scrolllistener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= SIZE_PAGE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem
                    && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getSearchedNews(binding.etSearch.text.toString())
                isScrolling = false
            }

        }
    }

    private fun settingListeners(){
        var job : Job? = null
        binding.etSearch.addTextChangedListener { inputText ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY_BETWEEN_SEARCH)
                inputText?.let {
                    if (inputText.toString().isNotEmpty()){
                        viewModel.getSearchedNews(inputText.toString())
                    }
                }
            }
        }

        articlesAdapter.setOnItemClickListener {  article->
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_ARTICLE, article)
            findNavController().navigate(
                    R.id.action_searchNewsFragment_to_articleFragment,
                    bundle
            )
        }
    }

    private fun showProgressbar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressbar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

}