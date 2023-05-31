package com.example.diadi.domain

import androidx.room.Embedded
import androidx.room.Relation

data class PlaceWithDiaries (
    @Embedded val place : Place,
    @Relation(
        parentColumn = "placeId",
        entityColumn = "place_id"
    )

    val diaries: List<Diary>
)