package com.mad.susach.timeline.data.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mad.susach.event.data.model.Event
import com.mad.susach.event.data.repository.EventRepository
import com.mad.susach.timeline.data.Era
import kotlinx.coroutines.tasks.await

class EraRepository {

    private val db = Firebase.firestore
    private val eventRepository = EventRepository()

    suspend fun getEras(): List<Era> {
        return try {
            val snapshot = db.collection("eras").get().await()
            val eras = snapshot.documents.mapNotNull { document ->
                document.toObject(Era::class.java)?.copy(id = document.id)
            }
            Log.d("EraRepository", "Fetched eras: $eras")
            eras
        } catch (e: Exception) {
            Log.e("EraRepository", "Error fetching eras", e)
            emptyList()
        }
    }

    suspend fun getEraById(id: String): Era? {
        return try {
            val doc = db.collection("eras").document(id).get().await()
            val era = doc.toObject(Era::class.java)?.copy(id = doc.id)
            Log.d("EraRepository", "Fetched era by id $id: $era")
            era
        } catch (e: Exception) {
            Log.e("EraRepository", "Error fetching era by id $id", e)
            null
        }
    }

    suspend fun getEventsForEra(eraId: String): List<Event> {
        return try {
            val events = eventRepository.getEventsForEra(eraId)
            Log.d("EraRepository", "Fetched events for era $eraId: $events")
            events
        } catch (e: Exception) {
            Log.e("EraRepository", "Error fetching events for era $eraId", e)
            emptyList()
        }
    }
}
