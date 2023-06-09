package com.example.diadi.repository

import com.example.diadi.dao.UserDao
import com.example.diadi.domain.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
){
    suspend fun insertUser(nickname : String) {
        suspend fun insertUser(nickname: String) {
            withContext(Dispatchers.IO) {
                val user = User(0, nickname)
                userDao.insertUser(user)
            }
        }
    }
}