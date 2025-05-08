package com.susach.repository

import com.susach.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepository {
    suspend fun getArticles(): List<Article> = withContext(Dispatchers.IO) {
        // TODO: Fetch articles from Firebase or Wikimedia API
        emptyList()
    }

    suspend fun getArticleById(id: String): Article? = withContext(Dispatchers.IO) {
        // TODO: Fetch a single article by ID
        null
    }

    suspend fun saveArticle(article: Article) {
        // TODO: Save article to Firebase
    }
}
