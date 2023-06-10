package com.example.diadi.repository

import com.example.diadi.dao.UserDao
import com.example.diadi.domain.User
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
){
    suspend fun insertUser(nickname : String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = User(0, nickname)
            userDao.insertUser(user)
        }
    }

    fun isNicknameExists(nickname: String): Boolean {
        val user = userDao.findUserByNickname(nickname)

        return user != null
    }
}