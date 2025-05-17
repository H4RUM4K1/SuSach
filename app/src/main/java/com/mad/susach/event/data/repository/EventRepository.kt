package com.mad.susach.event.data.repository

import com.mad.susach.event.data.model.Event
import kotlinx.coroutines.delay

// Placeholder repository for events
class EventRepository {

    // Simulates fetching an event by ID
    suspend fun getEventById(eventId: String): Event? {
        delay(1000) // Simulate network delay
        // Replace with actual data fetching logic (e.g., Firebase, Retrofit)
        return sampleEvents.find { it.id == eventId }
    }

    // Simulates fetching all events (e.g., for a list or search)
    suspend fun getAllEvents(): List<Event> {
        delay(1000) // Simulate network delay
        return sampleEvents
    }

    // Simulates fetching events for a specific era (if needed by other features)
    suspend fun getEventsForEra(eraId: String): List<Event> {
        delay(1000)
        return sampleEvents.filter { it.eraId == eraId }.sortedBy { it.year }
    }


    companion object {
        // Sample data - replace with your actual data source
        // Note: eraId is now nullable. Ensure your data reflects this if events can exist outside eras.
        val sampleEvents = listOf(
            Event(
                id = "e1", 
                eraId = "1", 
                name = "Kinh Dương Vương lập nước Xích Quỷ", 
                description = "Sự kiện đánh dấu sự khởi đầu của nhà nước sơ khai.", 
                year = -2879, 
                date = "Approx. 2879 BC",
                articleId = "a1"
            ),
            Event(
                id = "e2", 
                eraId = "2", 
                name = "An Dương Vương lập nước Âu Lạc", 
                description = "Thục Phán An Dương Vương thống nhất các bộ lạc Lạc Việt và Âu Việt.", 
                year = -257, 
                date = "Approx. 257 BC",
                articleId = "a2"
            ),
            Event(
                id = "e3", 
                eraId = "3", 
                name = "Khởi nghĩa Hai Bà Trưng", 
                description = "Cuộc khởi nghĩa chống lại ách đô hộ của nhà Hán.", 
                year = 40, 
                date = "40 AD",
                articleId = "a3"
            ),
            // Example of an event not tied to a specific era (eraId = null)
            Event(
                id = "e4", 
                eraId = null, 
                name = "Phát hiện trống đồng Đông Sơn", 
                description = "Phát hiện khảo cổ quan trọng minh chứng cho nền văn hóa Đông Sơn.", 
                year = 1924, // Example year
                date = "1924 AD",
                articleId = "a4"
            )
        )
    }
}
