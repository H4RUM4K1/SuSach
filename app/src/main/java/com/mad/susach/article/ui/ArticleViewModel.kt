package com.mad.susach.article.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.susach.event.data.Event
import com.mad.susach.event.data.EventRepository
import com.mad.susach.data.wikipedia.repository.WikipediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLDecoder

data class ArticleUiState(
    val isLoading: Boolean = true,
    val event: Event? = null,
    val errorMessage: String? = null,
    val fetchedWikipediaContent: String? = null
)

class ArticleViewModel(
    private val eventRepository: EventRepository = EventRepository(),
    private val wikipediaRepository: WikipediaRepository = WikipediaRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState

    private fun isWikipediaUrl(url: String?): Boolean {
        val pattern = "https://[a-z]{2,3}(\\.m)?\\.wikipedia\\.org/wiki/.*"

        if (url == null) {
            return false
        }
        // Trim the URL to remove any potential leading/trailing whitespace
        val trimmedUrl = url.trim()

        val regex = Regex(pattern)
        return trimmedUrl.matches(regex)
    }

    private fun extractPageTitleAndLangFromUrl(url: String): Pair<String, String>? {
        return try {
            val parsedUrl = URL(url)
            val hostParts = parsedUrl.host.split(".")
            val langCode: String?
            val pageTitlePath: String

            if (hostParts.size >= 3 && hostParts[1] == "m") {
                // Mobile URL like en.m.wikipedia.org
                langCode = hostParts[0]
            } else {
                // Standard URL like en.wikipedia.org
                langCode = hostParts.firstOrNull()
            }

            pageTitlePath = parsedUrl.path.substringAfter("/wiki/")
            val decodedPageTitle = URLDecoder.decode(pageTitlePath, "UTF-8")

            if (langCode != null && langCode.isNotBlank() && decodedPageTitle.isNotBlank()) {
                Pair(decodedPageTitle, langCode)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun fetchEventInternal(eventFetcher: suspend () -> Event?) {
        viewModelScope.launch {
            _uiState.value = ArticleUiState(isLoading = true)
            var fetchedEvent: Event? = null
            try {
                fetchedEvent = eventFetcher()
                if (fetchedEvent != null) {
                    if (isWikipediaUrl(fetchedEvent.contents)) {
                        val eventContents = fetchedEvent.contents!!
                        val extractedInfo = extractPageTitleAndLangFromUrl(eventContents)

                        if (extractedInfo != null) {
                            val (title, lang) = extractedInfo
                            try {
                                android.util.Log.d("ArticleViewModel", "Fetching Wikipedia content for title: $title, lang: $lang")
                                val wikiContent = wikipediaRepository.fetchWikipediaArticleHtml(title, lang)
                                if (wikiContent.isNullOrBlank()) {
                                    android.util.Log.w("ArticleViewModel", "Wikipedia content fetched is null or blank for title: $title")
                                    _uiState.value = ArticleUiState(isLoading = false, event = fetchedEvent, errorMessage = "Fetched Wikipedia content is empty.")
                                } else {
                                    android.util.Log.d("ArticleViewModel", "Successfully fetched Wikipedia content (first 200 chars): ${wikiContent.take(200)}")
                                    _uiState.value = ArticleUiState(isLoading = false, event = fetchedEvent, fetchedWikipediaContent = wikiContent)
                                }
                            } catch (e: Exception) {
                                _uiState.value = ArticleUiState(isLoading = false, event = fetchedEvent, errorMessage = "Failed to fetch Wikipedia HTML content: ${e.message}")
                            }
                        } else {
                            _uiState.value = ArticleUiState(isLoading = false, event = fetchedEvent, errorMessage = "Invalid Wikipedia URL format or could not extract language/title.")
                        }
                    } else {
                        _uiState.value = ArticleUiState(isLoading = false, event = fetchedEvent)
                    }
                } else {
                    _uiState.value = ArticleUiState(isLoading = false, errorMessage = "Event not found.")
                }
            } catch (e: Exception) {
                _uiState.value = ArticleUiState(isLoading = false, event = fetchedEvent, errorMessage = "Error: ${e.message}")
            }
        }
    }

    fun fetchEvent(eventId: String) {
        fetchEventInternal { eventRepository.getEventById(eventId) }
    }

    fun fetchRandomEvent() {
        fetchEventInternal { eventRepository.getRandomEvent() }
    }
}