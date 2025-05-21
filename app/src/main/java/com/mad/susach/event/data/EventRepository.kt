package com.mad.susach.event.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class EventRepository {
    private val db = Firebase.firestore

    //Lấy sự kiện theo ID
    suspend fun getEventById(eventId: String): Event? = try {
        val doc = db.collection("events").document(eventId).get().await()
        doc.toObject(Event::class.java)?.copy(id = doc.id)
    } catch (e: Exception) {
        null
    }

    //Lấy sự kiện theo tên
    suspend fun getEventByName(name: String): Event? {
        return try {
            val query = db.collection("events").whereEqualTo("name", name).get().await()
            val doc = query.documents.firstOrNull() ?: return null
            doc.toObject(Event::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }

    //Lấy danh sách sự kiện theo eraId.
    suspend fun getEventsForEra(eraId: String): List<Event> = try {
        val query = db.collection("events").whereEqualTo("eraId", eraId).get().await()
        query.documents.mapNotNull { it.toObject(Event::class.java)?.copy(id = it.id) }
    } catch (e: Exception) {
        emptyList()
    }

    //Lấy tất cả sự kiện.
    suspend fun getAllEvents(): List<Event> = try {
        val snapshot = db.collection("events").get().await()
        snapshot.documents.mapNotNull { it.toObject(Event::class.java)?.copy(id = it.id) }
    } catch (e: Exception) {
        emptyList()
    }

    //Lấy một sự kiện ngẫu nhiên.
    suspend fun getRandomEvent(): Event? = try {
        val allEvents = db.collection("events").get().await()
        if (allEvents.isEmpty) null
        else {
            val randomIndex = (0 until allEvents.size()).random()
            val randomDoc = allEvents.documents[randomIndex]
            randomDoc.toObject(Event::class.java)?.copy(id = randomDoc.id)
        }
    } catch (e: Exception) {
        null
    }
}
