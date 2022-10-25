package com.italo.pokerdroid.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.italo.pokerdroid.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class VotesRepositoryImpl(
    private val firestore: FirebaseFirestore
) : VotesRepository {

    override fun getVotes(): Flow<Boolean?> = callbackFlow {
        val snapshot = firestore.collection("game").document("Fgx3J3El6sswom7cilz0")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot!!.exists()) {
                    val showVotes = snapshot.getBoolean("showVotes")
                    this.trySend(showVotes)
                }
            }
        awaitClose { snapshot.remove() }
    }.flowOn(Dispatchers.IO)

    override fun getUsers(): Flow<List<User>> = callbackFlow {
        val snapshot = firestore.collection("game/Fgx3J3El6sswom7cilz0/user")
            .addSnapshotListener { snapshot, _ ->
                val users = snapshot?.toObjects(User::class.java)?.toList()!!
                this.trySend(users)
            }
        awaitClose { snapshot.remove() }
    }.flowOn(Dispatchers.IO)

    override fun vote(userId: String, vote: Long) {
        firestore.collection("game").document("Fgx3J3El6sswom7cilz0").collection("user")
            .document(userId).update("vote", vote)
        firestore.collection("game").document("Fgx3J3El6sswom7cilz0").collection("user")
            .document(userId).update("voted", true)
    }

    override fun showVotes() {
        firestore.collection("game").document("Fgx3J3El6sswom7cilz0").update("showVotes", true)
    }

    override fun resetVotes() {
        firestore.collection("game").document("Fgx3J3El6sswom7cilz0").update("showVotes", false)
        firestore.collection("game").document("Fgx3J3El6sswom7cilz0").collection("user").get()
            .addOnSuccessListener { snapshot ->
                snapshot.forEach {
                    it.reference.update("voted", false)
                    it.reference.update("vote", 0)
                }
            }
    }
}