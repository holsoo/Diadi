package com.example.diadi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.diadi.common.converter.Converters
import com.example.diadi.dao.DiaryDao
import com.example.diadi.dao.PlaceDao
import com.example.diadi.dao.UserDao
import com.example.diadi.domain.Diary
import com.example.diadi.domain.Place
import com.example.diadi.domain.User

@Database(entities = [User::class, Diary::class, Place::class], version = 1)
@TypeConverters(Converters::class)

abstract class DiadiDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun diaryDao(): DiaryDao
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile private var instance: DiadiDatabase ?= null

        fun getInstance(context: Context) : DiadiDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        @Override
        private fun buildDatabase(context: Context) : DiadiDatabase {
            return Room.databaseBuilder(context.applicationContext, DiadiDatabase::class.java, "diadi-database").build()
        }
    }
}