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

    @ColumnInfo(name = "road_address_name")
    val roadAddressName : String,

    @ColumnInfo(name = "category_group_name")
    val categoryGroupName : String,

    val longitude : String,

    val latitude : String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite : Boolean = false
)
