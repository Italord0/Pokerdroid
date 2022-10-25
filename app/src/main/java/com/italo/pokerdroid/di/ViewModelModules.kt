package com.italo.pokerdroid.di

import com.italo.pokerdroid.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainRepositoryModule = module {
    viewModel { MainViewModel(get()) }
}