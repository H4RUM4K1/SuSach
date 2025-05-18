package com.mad.susach.event.data.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mad.susach.event.data.model.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

// Repository for events
class EventRepository {
    private val db = Firebase.firestore

    // Fetches an event by ID
    suspend fun getEventById(eventId: String): Event? {
        return try {
            val doc = db.collection("events").document(eventId).get().await()
            val event = doc.toObject(Event::class.java)?.copy(id = doc.id)
            Log.d("EventRepository", "Fetched event by id $eventId: $event")
            event
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching event by id $eventId", e)
            null
        }
    }

    // Fetches an event by name
    suspend fun getEventByName(name: String): Event? {
        return try {
            val query = db.collection("events").whereEqualTo("name", name).get().await()
            val doc = query.documents.firstOrNull() ?: return null
            val event = doc.toObject(Event::class.java)?.copy(id = doc.id)
            Log.d("EventRepository", "Fetched event by name $name: $event")
            event
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching event by name $name", e)
            null
        }
    }

    // Fetches events for a specific era
    suspend fun getEventsForEra(eraId: String): List<Event> {
        return try {
            val query = db.collection("events").whereEqualTo("eraId", eraId).get().await()
            val events = query.documents.mapNotNull { it.toObject(Event::class.java)?.copy(id = it.id) }
            Log.d("EventRepository", "Fetched events for era $eraId: $events")
            events
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching events for era $eraId", e)
            emptyList()
        }
    }

    // Placeholder function to simulate network delay
    private suspend fun <T> simulateNetworkDelay(block: suspend () -> T): T {
        delay(1000) // Simulate network delay
        return block()
    }
}
