package com.example.news_solution.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news_solution.databinding.ItemArticleBinding
import com.example.news_solution.models.Article


class ArticlesAdapter: RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url // URL are unique; ID can be null
        }
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem //TODO Not sure. Verify later
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

       with(holder){

           with(differ.currentList[position]){
                binding.tvDescription.text = description
                binding.tvPublishedAt.text = publishedAt
               if (source != null) {
                   binding.tvSource.text = source?.name
               }
                binding.tvTitle.text = title

               Glide.with(holder.itemView).load(urlToImage).into(binding.ivArticleImage)

               holder.itemView.setOnClickListener{
                   onItemClickListener?.let { it(this) }
               }

           }
       }

    }

    override fun getItemCount(): Int = differ.currentList.size


}