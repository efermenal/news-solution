package com.example.news_solution.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_solution.MainActivity
import com.example.news_solution.NewsViewModel
import com.example.news_solution.R
import com.example.news_solution.adapters.ArticlesAdapter
import com.example.news_solution.databinding.FragmentSavedNewsBinding
import com.example.news_solution.utils.Constants.BUNDLE_ARTICLE
import timber.log.Timber

class DashboardFragment : Fragment() {

    private var _binding : FragmentSavedNewsBinding? = null
    private val binding: FragmentSavedNewsBinding get() = _binding!!
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initRecycler()
        settingObservers()
        viewModel.getArticlesSaved()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        articlesAdapter = ArticlesAdapter()
        binding.rvSavedNews.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        articlesAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable(BUNDLE_ARTICLE, article)
            }
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_articleFragment,
                bundle
            )
        }
    }

    private fun settingObservers() {
        viewModel.getArticlesSaved().observe(viewLifecycleOwner, Observer {
        articles ->
            articlesAdapter.differ.submitList(articles)
        })
    }

}