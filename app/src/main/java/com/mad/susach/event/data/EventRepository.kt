package com.mad.susach.event.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class EventRepository {
    private val db = Firebase.firestore

    // Fetch by IDI
    suspend fun getEventById(eventId: String): Event? {
        return try {
            val doc = db.collection("events").document(eventId).get().await()
            doc.toObject(Event::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching event by id $eventId", e)
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
            query.documents.mapNotNull { it.toObject(Event::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching events for era $eraId", e)
            emptyList()
        }
    }

    suspend fun getAllEvents(): List<Event> {
        return try {
            val snapshot = db.collection("events").get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Event::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRandomEvent(): Event? {
        return try {
            val allEventsCollection = db.collection("events").get().await()
            if (allEventsCollection.isEmpty) {
                null
            } else {
                val randomIndex = (0 until allEventsCollection.size()).random()
                val randomDocument = allEventsCollection.documents[randomIndex]
                randomDocument.toObject(Event::class.java)?.copy(id = randomDocument.id)
            }
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching random event", e)
            null
        }
    }
}
