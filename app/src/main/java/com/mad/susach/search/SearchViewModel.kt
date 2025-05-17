package com.mad.susach.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results
    private val db = FirebaseFirestore.getInstance()

    fun search(query: String) {
        viewModelScope.launch {
            db.collection("articles")
                .whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + '\uf8ff')
                .get()
                .addOnSuccessListener { documents ->
                    val list = documents.map { doc ->
                        SearchResult(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            year = doc.getString("year") ?: ""
                        )
                    }
                    _results.value = list
                }
        }
    }
}
