package com.italo.pokerdroid.di

import com.italo.pokerdroid.data.repository.VotesRepository
import com.italo.pokerdroid.data.repository.VotesRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mainViewModelModule = module {
    factoryOf(::VotesRepositoryImpl) { bind<VotesRepository>() }
}