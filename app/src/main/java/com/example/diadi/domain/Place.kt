package com.example.diadi.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
data class Place(
    @PrimaryKey(autoGenerate = true)
    val placeId : Int = 0,

    @ColumnInfo(name = "place_name")
    val placeName : String,

    @ColumnInfo(name = "address")
    val address : String,

    @ColumnInfo(name = "category")
    val category : String,

    val x : String, // longitude

    val y : String, // latitude

    @ColumnInfo(name = "is_favorite")
    val isFavorite : Boolean = false,

    @ColumnInfo(name = "diary_count")
    var diaryCount: Int = 0
) {
    fun incrementDiaryCount() {
        diaryCount++
    }
}

