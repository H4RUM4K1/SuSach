package com.susach.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: String = "",                // Firebase document ID
    val title: String = "",
    val group: String = "",             // "Event" or "Historical Figure"
    val wikiSite: String = "",          // e.g., "vi.wikipedia.org"
    val fragment: String? = null,       // Section anchor
    val imageUrl: String? = null,
    val description: String? = null,
    val extract: String? = null,        // Short summary
    val content: String? = null,        // Full article content
    val sections: List<ArticleSection> = emptyList(),
    val customization: ArticleCustomization = ArticleCustomization(),
    val comments: List<Comment> = emptyList(),
    val likes: Int = 0,
    val views: Int = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) : Parcelable

@Parcelize
data class ArticleSection(
    val heading: String,
    val anchor: String
) : Parcelable

@Parcelize
data class ArticleCustomization(
    val backgroundColor: String = "#FFFFFF",
    val fontSize: Int = 16,
    val fontFamily: String = "sans-serif"
) : Parcelable

@Parcelize
data class Comment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val likes: Int = 0,
    val replies: List<Comment> = emptyList(),
    val createdAt: Long = 0L
) : Parcelable
