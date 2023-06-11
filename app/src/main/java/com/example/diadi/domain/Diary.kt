package com.example.diadi.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.diadi.common.enums.Weathers
import java.util.Date

@Entity(tableName = "diary",
    foreignKeys = [
        ForeignKey(
            entity = Place::class,
            parentColumns = ["placeId"],
            childColumns = ["place_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["place_id"])]
)

data class Diary (
    @PrimaryKey(autoGenerate = true)
    val diaryId: Long = 0,

    val title: String,

    val content: String,

    val imageUrl : String,

    val weather : Weathers,

    @ColumnInfo(name = "place_id")
    var placeId : Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)

