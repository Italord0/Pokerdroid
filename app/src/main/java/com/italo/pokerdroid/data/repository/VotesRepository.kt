package com.italo.pokerdroid.data.repository

import com.italo.pokerdroid.data.model.User
import kotlinx.coroutines.flow.Flow

interface VotesRepository {
    fun getVotes() : Flow<Boolean?>
    fun getUsers() : Flow<List<User>>
    fun vote(userId : String, vote : Long)
    fun showVotes()
    fun resetVotes()
}
