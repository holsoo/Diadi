package com.example.diadi.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.diadi.domain.Diary
import com.example.diadi.domain.DiaryWithPlace
import com.example.diadi.domain.PlaceWithDiaries

@Dao
interface DiaryDao {

    @Transaction
    @Query("SELECT * FROM DIARY WHERE place_id = :placeId")
    fun findDiariesByPlaceId(placeId: Long): List<DiaryWithPlace>

    @Query("SELECT * FROM DIARY WHERE diaryId = :diaryId")
    fun findDiaryById(diaryId: Long): Diary

    @Query("SELECT * FROM DIARY")
    fun findAllDiary() : List<Diary>

    @Transaction
    @Query("SELECT * FROM place WHERE x = :x AND y = :y")
    fun getPlaceWithDiaries(x: Double, y: Double): List<PlaceWithDiaries>

    @Insert
    fun insertDiary(diary: Diary)

    @Update
    fun updateDiary(diary: Diary)

    @Delete
    fun deleteDiary(diary: Diary)
}