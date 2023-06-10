package com.example.diadi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diadi.common.enums.Weathers
import com.example.diadi.repository.DiaryRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
): ViewModel() {
    fun insertDiary (diaryId: Long, title: String, content: String, imageUrl: String, weather: Weathers, createdAt: Date, userId: Long, placeId: Long) {
        viewModelScope.launch {
            diaryRepository.insertDiary(diaryId, title, content, imageUrl, weather, createdAt, userId, placeId)
        }
    }
}