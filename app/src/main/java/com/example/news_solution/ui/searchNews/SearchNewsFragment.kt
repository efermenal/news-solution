package com.example.news_solution.ui.searchNews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_solution.MainActivity
import com.example.news_solution.NewsViewModel
import com.example.news_solution.R
import com.example.news_solution.adapters.ArticlesAdapter
import com.example.news_solution.databinding.FragmentSearchNewsBinding
import com.example.news_solution.utils.Constants.BUNDLE_ARTICLE
import com.example.news_solution.utils.Constants.DELAY_BETWEEN_SEARCH
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding: FragmentSearchNewsBinding
        get() = _binding!!

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
        }
    }

    private fun settingObservers() {
        viewModel.searchNews.removeObservers(viewLifecycleOwner)
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            articlesAdapter.differ.submitList(response.data?.articles)
        })
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

}