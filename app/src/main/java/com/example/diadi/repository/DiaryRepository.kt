package com.example.diadi.repository

import com.example.diadi.dao.DiaryDao
import com.example.diadi.domain.Diary
import com.example.diadi.dto.CreateDiaryDto
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val diaryDao: DiaryDao
){
    fun insertDiary(createDiaryDto: CreateDiaryDto) {
        CoroutineScope(Dispatchers.IO).launch {
            val diary = createDiaryDto.toEntity()
            diaryDao.insertDiary(diary)
        }
    }

    fun deleteDiary(id : Long) {
        CoroutineScope(Dispatchers.IO).launch {
            var diary : Diary = diaryDao.findDiaryById(id)
            diaryDao.deleteDiary(diary)
        }
    }
}
