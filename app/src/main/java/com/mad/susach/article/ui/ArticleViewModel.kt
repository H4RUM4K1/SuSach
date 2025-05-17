package com.mad.susach.article.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.article.data.model.Article
import com.mad.susach.article.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val articleRepository: ArticleRepository = ArticleRepository() // Replace with DI
) : ViewModel() {

    private val _article = MutableStateFlow<Article?>(null)
    val article: StateFlow<Article?> = _article

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadArticle(articleId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _article.value = articleRepository.getArticleById(articleId)
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            }
            _isLoading.value = false
        }
    }
}
