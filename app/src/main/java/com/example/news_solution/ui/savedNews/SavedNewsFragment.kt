package com.example.news_solution.ui.savedNews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news_solution.MainActivity
import com.example.news_solution.NewsViewModel
import com.example.news_solution.R
import com.example.news_solution.adapters.ArticlesAdapter
import com.example.news_solution.databinding.FragmentSavedNewsBinding
import com.example.news_solution.utils.Constants.BUNDLE_ARTICLE
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment() {

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

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = articlesAdapter.differ.currentList[position]
                viewModel.deleteNew(article)
                Snackbar.make(view, "The article was deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

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
                R.id.action_navigation_saved_news_to_articleFragment,
                bundle
            )
        }
    }

    private fun settingObservers() {
        viewModel.getArticlesSaved().removeObservers(viewLifecycleOwner)
        viewModel.getArticlesSaved().observe(viewLifecycleOwner, Observer {
        articles ->
            articlesAdapter.differ.submitList(articles)
        })
    }

}