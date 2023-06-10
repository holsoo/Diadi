package com.example.diadi.dto

import com.example.diadi.common.enums.Weathers
import com.example.diadi.domain.Diary

data class CreateDiaryDto(
    var diaryId: Long,
    var title: String,
    var content: String,
    var imageUrl: String,
    var weather: Weathers,
    var userId: Long,
    var placeId: Long
) {
    fun toEntity(): Diary {
        return Diary(
            diaryId = diaryId,
            title = title,
            content = content,
            imageUrl = imageUrl,
            weather = weather,
            userId = userId,
            placeId = placeId
        )
    }
}

