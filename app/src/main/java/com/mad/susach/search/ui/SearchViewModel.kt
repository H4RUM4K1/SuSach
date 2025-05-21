package com.mad.susach.search.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.Event
import com.mad.susach.event.data.EventRepository
import com.mad.susach.timeline.data.Era
import com.mad.susach.timeline.data.EraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _results = MutableStateFlow<List<Event>>(emptyList())
    val results: StateFlow<List<Event>> = _results

    private val eventRepository = EventRepository()
    private val eraRepository = EraRepository()

    private val _eras = MutableStateFlow<List<Era>>(emptyList())
    val eras: StateFlow<List<Era>> = _eras

    var selectedEraId by mutableStateOf<String?>(null)
        private set

    var selectedSort by mutableStateOf("A-Z")
        private set

    private var currentQuery: String = "" // Store current query to re-apply search when era changes

    init {
        fetchEras()
        search("") // Initial search to load all events or based on default filters
    }

    private fun fetchEras() {
        viewModelScope.launch {
            _eras.value = eraRepository.getEras()
        }
    }

    fun setSelectedEra(eraId: String?) {
        selectedEraId = eraId
        search(currentQuery) // Re-run search with the new era and existing query
    }

    fun setSort(sort: String) {
        selectedSort = sort
        sortResults()
    }

    private fun sortResults() {
        val current = _results.value
        val sorted = when (selectedSort) {
            "A-Z" -> current.sortedBy { it.name }
            "Mới nhất" -> current.sortedByDescending { it.startDate } // Assuming Event has startDate
            "Xưa nhất" -> current.sortedBy { it.startDate }         // Assuming Event has startDate
            else -> current
        }
        _results.value = sorted
    }

    fun search(query: String) {
        currentQuery = query.trim() // Update current query
        val normalizedQuery = currentQuery.lowercase()

        viewModelScope.launch {
            val allEvents = eventRepository.getAllEvents()

            val eventsFilteredByEra = if (selectedEraId != null) {
                allEvents.filter { it.eraId == selectedEraId }
            } else {
                allEvents
            }

            val finalResults = if (normalizedQuery.isNotEmpty()) {
                eventsFilteredByEra.filter { event ->
                    event.name.lowercase().contains(normalizedQuery) ||
                            (event.description.lowercase().contains(normalizedQuery)) // Removed unnecessary safe call as description is non-null
                }
            } else {
                eventsFilteredByEra
            }
            _results.value = finalResults
            sortResults() // Apply sorting after search
        }
    }
}
