package com.example.diadi.common.di

import android.app.Application
import android.content.Context
import com.example.diadi.dao.UserDao
import com.example.diadi.database.DiadiDatabase
import com.example.diadi.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideUserDao(context: Context): UserDao {
        return DiadiDatabase.getInstance(context).userDao()
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}