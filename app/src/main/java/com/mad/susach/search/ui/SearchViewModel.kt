package com.mad.susach.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.susach.event.data.model.Event
import com.mad.susach.event.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun formatEventDate(start: Int, end: Int): String = if (start == end || end == 0) start.toString() else "$start - $end"

class SearchViewModel : ViewModel() {
    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results
    private val db = FirebaseFirestore.getInstance()
    private val eventRepository = EventRepository()

    var selectedType by mutableStateOf("Bất kì")
        private set

    var selectedSort by mutableStateOf("A-Z")
        private set

    fun setType(type: String) {
        selectedType = type
    }

    fun setSort(sort: String) {
        selectedSort = sort
        // Trigger re-sort after changing sort type
        sortResults()
    }

    private fun sortResults() {
        val current = _results.value
        val sorted = when (selectedSort) {
            "A-Z" -> current.sortedBy { it.title }
            "Mới nhất" -> current.sortedByDescending { extractStartDate(it.year) }
            "Xưa nhất" -> current.sortedBy { extractStartDate(it.year) }
            else -> current
        }
        _results.value = sorted
    }

    fun search(query: String) {
        val normalizedQuery = query.trim().lowercase()
        viewModelScope.launch {
            if (selectedType == "Sự kiện") {
                val events = db.collection("events").get().await()
                val list = events.documents.mapNotNull { doc ->
                    val event = doc.toObject(Event::class.java)?.copy(id = doc.id)
                    if (event != null && event.name.lowercase().contains(normalizedQuery)) {
                        SearchResult(
                            id = event.id,
                            title = event.name,
                            year = formatEventDate(event.startDate, event.endDate)
                        )
                    } else null
                }
                _results.value = list
                sortResults()
            } else if (selectedType == "Bất kì") {
                val articlesDeferred = db.collection("articles").get()
                val eventsDeferred = db.collection("events").get()
                val articles = articlesDeferred.await()
                val events = eventsDeferred.await()
                val articleResults = articles.documents.mapNotNull { doc ->
                    val title = doc.getString("title") ?: ""
                    if (title.lowercase().contains(normalizedQuery)) {
                        SearchResult(
                            id = doc.id,
                            title = title,
                            year = doc.getString("year") ?: ""
                        )
                    } else null
                }
                val eventResults = events.documents.mapNotNull { doc ->
                    val event = doc.toObject(Event::class.java)?.copy(id = doc.id)
                    if (event != null && event.name.lowercase().contains(normalizedQuery)) {
                        SearchResult(
                            id = event.id,
                            title = event.name,
                            year = formatEventDate(event.startDate, event.endDate)
                        )
                    } else null
                }
                _results.value = articleResults + eventResults
                sortResults()
            } else {
                val articles = db.collection("articles").get().await()
                val list = articles.documents.mapNotNull { doc ->
                    val title = doc.getString("title") ?: ""
                    if (title.lowercase().contains(normalizedQuery)) {
                        SearchResult(
                            id = doc.id,
                            title = title,
                            year = doc.getString("year") ?: ""
                        )
                    } else null
                }
                _results.value = list
                sortResults()
            }
        }
    }

    private fun extractStartDate(year: String): Int {
        // Expecting year in format "start - end" or just "start"
        return year.split("-").firstOrNull()?.trim()?.toIntOrNull() ?: 0
    }
}
