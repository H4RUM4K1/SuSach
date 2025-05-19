package com.mad.susach.timeline.data.repository

//import com.mad.susach.article.data.model.Article // Assuming Article model is in article feature
import com.mad.susach.timeline.data.model.Era
import com.mad.susach.event.data.repository.EventRepository // Corrected import
import com.mad.susach.event.data.model.Event // Corrected import

class TimelineRepository {

    // Eras are specific to the timeline feature
    private val eras = listOf(
        Era(id = "1", name = "Thời kỳ Hồng Bàng", startYear = -2879, endYear = -258),
        Era(id = "2", name = "Thời kỳ Âu Lạc và Nam Việt", startYear = -257, endYear = -111),
        Era(id = "3", name = "Thời kỳ Bắc thuộc lần thứ nhất", startYear = -111, endYear = 40)
        // Add more eras as needed
    )

    // Inject or get an instance of EventRepository
    // For simplicity, creating an instance here. In a real app, use DI.
    private val eventRepository = EventRepository()
//    private val articles = listOf(
//        Article(id = "a1", title = "Kinh Dương Vương và Nước Xích Quỷ", content = "Chi tiết về Kinh Dương Vương và nước Xích Quỷ..."),
//        Article(id = "a2", title = "An Dương Vương và Nước Âu Lạc", content = "Chi tiết về An Dương Vương và nước Âu Lạc, thành Cổ Loa..."),
//        Article(id = "a3", title = "Khởi Nghĩa Hai Bà Trưng", content = "Diễn biến và ý nghĩa của cuộc khởi nghĩa Hai Bà Trưng...")
//        // Add more articles
//    )

    fun getEras(): List<Era> {
        return eras
    }

    // Delegate to EventRepository for fetching events for an era
    suspend fun getEventsForEra(eraId: String): List<Event> {
        return eventRepository.getEventsForEra(eraId)
    }

    // This method might be removed if Article handling is fully in ArticleRepository
    // Or it could fetch from an ArticleRepository if needed for timeline-specific logic
//    fun getArticleById(articleId: String): Article? {
//        // TODO: Decide if this should delegate to an ArticleRepository
//        return articles.find { it.id == articleId }
//    }
//
//    fun getEraById(eraId: String): Era? {
//        return eras.find { it.id == eraId }
//    }

    // getEventById is now in EventRepository, TimelineRepository doesn't need it directly
    // unless there's specific timeline logic when fetching a single event.
    // suspend fun getEventById(eventId: String): Event? {
    //     return eventRepository.getEventById(eventId)
    // }
}
