package com.example.news_solution.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news_solution.MainActivity
import com.example.news_solution.NewsViewModel
import com.example.news_solution.R
import com.example.news_solution.adapters.ArticlesAdapter
import com.example.news_solution.databinding.FragmentHomeBinding
import com.example.news_solution.utils.Constants
import com.example.news_solution.utils.Constants.BUNDLE_ARTICLE
import com.example.news_solution.utils.Resource
import dagger.android.support.DaggerFragment
import timber.log.Timber


class HomeFragment : DaggerFragment() {

    lateinit var articlesAdapter : ArticlesAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    private var isLoading = false
    private var isScrolling = false
    private var isLastPage = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        settingObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        articlesAdapter = ArticlesAdapter()
        binding.rvBreakingNews.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrolllistener)
        }

        articlesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(BUNDLE_ARTICLE, it)
            }

            findNavController().navigate(
                R.id.action_navigation_home_to_articleFragment,
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

    private fun settingObservers(){
        viewModel.breakingNews.removeObservers(viewLifecycleOwner)
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showProgressbar()
                    Timber.d("Showing progressbar")
                }

                is Resource.Success -> {
                    hideProgressbar()
                    response.data?.let { newsResponse->
                        articlesAdapter.differ.submitList(newsResponse.articles.toList())
                        Timber.d("List sent: %s",newsResponse.articles.size)
                        val totalPages = newsResponse.totalResults / Constants.SIZE_PAGE + 2
                        isLastPage = viewModel.numberPageBreakingNews == totalPages
                        if (isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressbar()
                    response.message?.let { it -> Timber.d("An error occurred $it") }
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.SIZE_PAGE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem
                    && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }

        }
    }

}