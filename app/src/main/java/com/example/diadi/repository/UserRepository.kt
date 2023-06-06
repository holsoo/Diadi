package com.example.diadi.repository

import com.example.diadi.dao.UserDao
import com.example.diadi.domain.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(nickname : String) {
        val user = User(0, nickname)
        userDao.insertUser(user)
    }
}