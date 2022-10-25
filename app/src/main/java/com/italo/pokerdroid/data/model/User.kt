package com.italo.pokerdroid.data.model

import com.google.firebase.firestore.PropertyName

data class User(
    @get:PropertyName("name")
    val name : String = "",
    @get:PropertyName("vote")
    val vote : Int = 0,
    @get:PropertyName("voted")
    val voted : Boolean = false
)
