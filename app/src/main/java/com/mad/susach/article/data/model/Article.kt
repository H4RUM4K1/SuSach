package com.mad.susach.article.data.model

// Re-using the existing Article model, but in its new package
// If you need a different model for the article feature, define it here.
// For now, we assume the existing one is sufficient.

// Original content from com.mad.susach.timeline.data.model.Article.kt:
data class Article(
    val id: String,
    val title: String,
    val content: String,
    val imageUrls: List<String>? = null,
    val source: String? = null
)
