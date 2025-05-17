package com.mad.susach.article.data.repository

import com.mad.susach.article.data.model.Article
import kotlinx.coroutines.delay

// Placeholder repository for articles
class ArticleRepository {

    // Simulates fetching an article by ID
    suspend fun getArticleById(articleId: String): Article? {
        delay(1000) // Simulate network delay
        // Replace with actual data fetching logic (e.g., Firebase, Retrofit)
        return sampleArticles.find { it.id == articleId }
    }

    // Simulates fetching all articles (e.g., for a list or search)
    suspend fun getAllArticles(): List<Article> {
        delay(1000) // Simulate network delay
        return sampleArticles
    }

    companion object {
        // Sample data - replace with your actual data source
        private val sampleArticles = listOf(
            Article(
                id = "article1",
                title = "The First Historical Article",
                content = "This is the full content of the first historical article. It details important events and figures...",
                imageUrls = listOf("https://example.com/image1.jpg"),
                source = "Historical Archives Vol. 1"
            ),
            Article(
                id = "article2",
                title = "Another Significant Story",
                content = "Delving deep into another significant story from the past, this article provides context and analysis...",
                imageUrls = listOf("https://example.com/image2.jpg", "https://example.com/image3.jpg"),
                source = "Journal of Historical Studies"
            )
            // Add more sample articles as needed
        )
    }
}
