package com.susach.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susach.model.Article
import com.susach.repository.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: ArticleRepository = ArticleRepository()) : ViewModel() {
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> = _selectedArticle

    fun loadArticles() {
        viewModelScope.launch {
            _articles.value = repository.getArticles()
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun loadArticleById(id: String) {
        viewModelScope.launch {
            _selectedArticle.value = repository.getArticleById(id)
        }
    }
}
