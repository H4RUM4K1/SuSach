package com.mad.susach.timeline.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mad.susach.event.data.Event
import com.mad.susach.event.data.EventRepository
import kotlinx.coroutines.tasks.await

class EraRepository {

    private val db = Firebase.firestore
    private val eventRepository = EventRepository()

    // Lấy tất cả era từ Firestore
    suspend fun getEras(): List<Era> = try {
        db.collection("eras")
            .get()
            .await()
            .documents
            .mapNotNull { doc -> doc.toObject(Era::class.java)?.copy(id = doc.id) }
    } catch (e: Exception) {
        emptyList()
    }

    // Lấy 1 era theo id
    suspend fun getEraById(id: String): Era? = try {
        db.collection("eras")
            .document(id)
            .get()
            .await()
            .toObject(Era::class.java)
            ?.copy(id = id)
    } catch (e: Exception) {
        null
    }

    // Lấy tất cả các sự kiện trong một era
    suspend fun getEventsForEra(eraId: String): List<Event> = try {
        eventRepository.getEventsForEra(eraId)
    } catch (e: Exception) {
        emptyList()
    }
}
