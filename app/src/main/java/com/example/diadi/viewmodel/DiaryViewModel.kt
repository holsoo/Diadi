package com.example.diadi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diadi.domain.PlaceWithDiaries
import com.example.diadi.dto.CreateDiaryDto
import com.example.diadi.repository.DiaryRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
): ViewModel() {

    fun createDiary (createDiaryDto: CreateDiaryDto) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryRepository.insertDiary(createDiaryDto)
        }
    }

    fun getPlaceWithDiaries(x: Double, y: Double): PlaceWithDiaries
    {
        return diaryRepository.getPlaceWithDiaries(x, y)
    }

    fun deleteDiary (id : Long) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryRepository.deleteDiary(id)
        }
    }
}
