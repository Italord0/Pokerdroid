package com.italo.pokerdroid.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.italo.pokerdroid.data.model.GameState
import com.italo.pokerdroid.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class VotesRepositoryImpl(
    private val firestore: FirebaseFirestore
) : VotesRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUsers(): Flow<List<User>?> = callbackFlow {
        val snapshot = firestore.collection("game/Fgx3J3El6sswom7cilz0/user").addSnapshotListener { snapshot, _ ->
            val users = snapshot?.toObjects(User::class.java)?.toList()
            this.trySend(users)
        }
        awaitClose { snapshot.remove() }
    }.flowOn(Dispatchers.IO)
}