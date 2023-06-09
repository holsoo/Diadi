package com.example.diadi.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = true)
    val userId : Int = 0,

    val nickname : String,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
