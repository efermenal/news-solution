package com.example.news_solution.ui.common


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.news_solution.MainActivity
import com.example.news_solution.NewsViewModel
import com.example.news_solution.databinding.FragmentArticleBinding
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment() {

    private var _binding : FragmentArticleBinding? = null
    private val binding : FragmentArticleBinding get() = _binding!!
    private val args : ArticleFragmentArgs by navArgs()
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "The article was saved", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}