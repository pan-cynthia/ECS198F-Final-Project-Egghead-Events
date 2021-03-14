package com.egghead.events

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class EventFirestore {
    companion object {
        fun postFavorite(event: Event) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            val firebaseInstance = FirebaseAuth.getInstance()
            if (firebaseInstance.currentUser != null) {
                firestore.collection("users").document(firebaseInstance.currentUser.uid)
                    .get()
            }

        }

        fun postEvent(event: Event) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            firestore.collection("events").add(event)
                .addOnSuccessListener { Log.d("API", "Event successfully written!") }
                .addOnFailureListener { Log.d("API", "Event failed to write!") }
        }

        fun updateEvent(event: Event) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            firestore.collection("events").document(event.documentId)
                .set(event)
                .addOnSuccessListener { Log.d("API", "Event successfully written!") }
                .addOnFailureListener { Log.d("API", "Event failed to write!") }
        }

        fun getAllEvents(setEvents: (events: ArrayList<Event>) -> Unit) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            val events = arrayListOf<Event>()

            firestore.collection("events")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val event = document.toObject(Event::class.java)
                        events.add(event)
                    }
                    setEvents(events)
                }
                .addOnFailureListener { Log.d("API", "Event failed to write!") }
        }
    }
}