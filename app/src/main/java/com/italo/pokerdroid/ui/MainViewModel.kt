package com.italo.pokerdroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.italo.pokerdroid.data.model.GameState
import com.italo.pokerdroid.data.model.User
import com.italo.pokerdroid.data.repository.VotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val votesRepository: VotesRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(
        GameState()
    )

    val gameState = _gameState.asStateFlow()

    init {
        getVotes()
    }

    private fun getVotes() {
        viewModelScope.launch {
            _gameState.emitAll(
                votesRepository.getVotes().map(::handleShowVotes)
            )
        }
        viewModelScope.launch {
            _gameState.emitAll(
                votesRepository.getUsers().map(::handleUsers)
            )
        }
    }

    private fun handleShowVotes(showVotes: Boolean?): GameState {
        return gameState.value.copy(showVotes = showVotes)
    }

    private fun handleUsers(users: List<User>): GameState {
        return gameState.value.copy(users = users)
    }

    fun vote(userId: String, vote: Int) {
        votesRepository.vote(userId,vote.toLong())
    }

    fun showVotes() {
        votesRepository.showVotes()
    }

    fun resetVotes() {
        votesRepository.resetVotes()
    }

}