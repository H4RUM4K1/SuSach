package com.mad.susach.saved.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.Event
import com.mad.susach.saved.data.SavedPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedPostsViewModel(
    private val repository: SavedPostRepository = SavedPostRepository()
) : ViewModel() {
    private val _savedPosts = MutableStateFlow<List<Event>>(emptyList())
    val savedPosts: StateFlow<List<Event>> = _savedPosts.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentSort = SortOption.SavedTime

    init {
        loadSavedPosts()
    }

    private fun loadSavedPosts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val posts = repository.getSavedPosts(currentSort)
                _savedPosts.value = posts
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSort(sortOption: SortOption) {
        currentSort = sortOption
        loadSavedPosts()
    }
}
