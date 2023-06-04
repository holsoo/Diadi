package com.example.diadi.domain

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.diadi.common.enums.Weathers
import java.time.LocalDateTime

@Entity(foreignKeys = [
    ForeignKey(
        entity = Place::class,
        parentColumns = ["placeId"],
        childColumns = ["place_id"]
    ),

    ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["user_id"]
    ),
])
data class Diary (
    @PrimaryKey(autoGenerate = true)
    val diaryId: Long,

    val title: String,

    val content: String,

    val image : Bitmap? = null,

    val weather : Weathers,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "user_id")
    var userId : Long,

    @ColumnInfo(name = "place_id")
    var placeId : Long
    )

