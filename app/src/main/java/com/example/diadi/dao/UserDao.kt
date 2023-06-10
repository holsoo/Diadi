package com.example.diadi.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.example.diadi.domain.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE userId = :userId")
    fun findUserById(userId : Long): User?

    @Query("SELECT * FROM User WHERE nickname = :nickname")
    fun findUserByNickname(nickname : String): User?

    @Query("SELECT * FROM User")
    fun findAllUser(): List<User>

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}
