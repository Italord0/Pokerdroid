package com.italo.pokerdroid.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class User(
    @DocumentId
    var id : String? = "",
    @get:PropertyName("name")
    val name : String? = "",
    @get:PropertyName("vote")
    val vote : Long? = 0,
    @get:PropertyName("voted")
    val voted : Boolean? = false
)
