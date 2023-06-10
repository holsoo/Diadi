package com.example.diadi.viewmodel

import androidx.lifecycle.ViewModel
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
}
