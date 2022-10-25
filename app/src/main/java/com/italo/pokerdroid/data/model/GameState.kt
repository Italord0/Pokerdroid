package com.italo.pokerdroid.data.model

data class GameState(
    val showVotes : Boolean? = false,
    val users : List<User> = listOf()
)
