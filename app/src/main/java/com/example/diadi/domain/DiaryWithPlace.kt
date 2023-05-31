package com.example.diadi.domain

import androidx.room.Embedded
import androidx.room.Relation

data class DiaryWithPlace(
    @Embedded
    val diary: Diary,

    @Relation(
        parentColumn = "place_id",
        entityColumn = "placeId"
    )

    val place: Place
)
