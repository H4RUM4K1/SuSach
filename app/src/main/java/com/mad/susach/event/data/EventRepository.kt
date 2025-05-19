package com.mad.susach.event.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class EventRepository {
    private val db = Firebase.firestore

    // Fetch by IDI
    suspend fun getEventById(eventId: String): Event? {
        return try {
            val doc = db.collection("events").document(eventId).get().await()
            val event = doc.toObject(Event::class.java)?.copy(id = doc.id)
            event
        } catch (e: Exception) {
            null
        }
    }

    // Fetch by name
    suspend fun getEventByName(name: String): Event? {
        return try {
            val query = db.collection("events").whereEqualTo("name", name).get().await()
            val doc = query.documents.firstOrNull() ?: return null
            val event = doc.toObject(Event::class.java)?.copy(id = doc.id)
            event
        } catch (e: Exception) {
            null
        }
    }

    // Fetches events for a specific era
    suspend fun getEventsForEra(eraId: String): List<Event> {
        return try {
            val query = db.collection("events").whereEqualTo("eraId", eraId).get().await()
            val events = query.documents.mapNotNull { it.toObject(Event::class.java)?.copy(id = it.id) }
            events
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRandomEvent(): Event? = try {
        val allEvents = db.collection("events").get().await()
        if (allEvents.isEmpty) {
            null
        } else {
            val randomIndex = (0 until allEvents.size()).random()
            val randomDocument = allEvents.documents[randomIndex]
            randomDocument.toObject(Event::class.java)?.copy(id = randomDocument.id)
        }
    } catch (e: Exception) {
        null
    }

    // Placeholder function to simulate network delay
    private suspend fun <T> simulateNetworkDelay(block: suspend () -> T): T {
        delay(1000) // Simulate network delay
        return block()
    }
}
