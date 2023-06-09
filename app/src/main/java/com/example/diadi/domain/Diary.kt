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
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"]
        )
    ],
    indices = [Index(value = ["place_id"])]
)
data class Diary (
    @PrimaryKey(autoGenerate = true)
    val diaryId: Int = 0,

    val title: String,

    val content: String,

    val imageUrl : String,

    val weather : Weathers,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "user_id")
    var userId : Long,

    @ColumnInfo(name = "place_id")
    var placeId : Long
    )

