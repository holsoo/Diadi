package com.example.diadi.common.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ProviderModule::class])
interface AppComponent {
}