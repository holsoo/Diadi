package com.example.diadi.repository

import com.example.diadi.common.enums.Weathers
import com.example.diadi.dao.DiaryDao
import com.example.diadi.domain.Diary

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val diaryDao: DiaryDao
){
    suspend fun insertDiary(diaryId: Long, title: String, content: String, imageUrl: String, weather: Weathers, createdAt: Date, userId: Long, placeId: Long) {
        suspend fun insertDiary(diaryId: Long, title: String, content: String, imageUrl: String, weather: Weathers, createdAt: Date, userId: Long, placeId: Long) {
            withContext(Dispatchers.IO) {
                val diary = Diary(0, title, content, imageUrl, weather, createdAt, userId, placeId)
                diaryDao.findDiariesByPlaceId(placeId)
                diaryDao.findDiaryById(diaryId)
                diaryDao.findAllDiary()
                diaryDao.insertDiary(diary)
                diaryDao.updateDiary(diary)
            }
        }
    }
}