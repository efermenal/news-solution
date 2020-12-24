package com.example.news_solution.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_solution.adapters.ArticlesAdapter
import com.example.news_solution.databinding.FragmentHomeBinding
import com.example.news_solution.utils.Resource
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    lateinit var articlesAdapter : ArticlesAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //homeFragment =  viewModel(viewModelFactory) TODO: search for Fernando Cejas explanation
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        Timber.d("VARIABLE VALUE %s", viewModel)
    }

    private fun settingObservers(){
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showProgressbar()
                }

                is Resource.Success -> {
                    hideProgressbar()
                    response.data?.let { newsResponse ->
                        articlesAdapter.differ.submitList(
                            newsResponse.articles

                        )
                    }
                }
                is Resource.Error -> {
                    hideProgressbar()
                    response.message?.let { it -> Timber.d("An error occurred $it") }
                }

            }
        })
    }

    private fun showProgressbar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
      binding.paginationProgressBar.visibility = View.INVISIBLE
    }

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
        }


    }
}