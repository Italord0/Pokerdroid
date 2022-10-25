package com.italo.pokerdroid.di

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val commonModule = module {
    single { FirebaseFirestore.getInstance() }
}