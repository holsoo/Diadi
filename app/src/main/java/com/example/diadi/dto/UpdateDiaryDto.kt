package com.example.diadi.dto

import com.example.diadi.common.enums.Weathers
import com.example.diadi.domain.Diary

data class UpdateDiaryDto(
    var title: String,
    var content: String,
    var imageUrl: String,
    var weather: Weathers,
    var date : String,
    var placeId: Int
) {
    fun toEntity(): Diary {
        return Diary(
            diaryId = 0,
            title = title,
            content = content,
            imageUrl = imageUrl,
            weather = weather,
            placeId = placeId
        )
    }
}
