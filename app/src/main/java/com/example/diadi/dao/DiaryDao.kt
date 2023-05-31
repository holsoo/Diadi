package com.example.diadi.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.diadi.domain.Diary
import com.example.diadi.domain.DiaryWithPlace

@Dao
interface DiaryDao {

    @Transaction
    @Query("SELECT * FROM DIARY WHERE place_id = :placeId")
    fun findDiariesByPlaceId(placeId: Long): List<DiaryWithPlace>

    @Query("SELECT * FROM DIARY WHERE diaryId = :diaryId")
    fun findDiaryById(diaryId: Long): List<Diary>

    @Query("SELECT * FROM DIARY")
    fun findAllDiary()

    @Insert
    fun insertDiary(diary: Diary)

    @Update
    fun updateDiary(diary: Diary)

    @Delete
    fun deleteDiary(diary: Diary)
}