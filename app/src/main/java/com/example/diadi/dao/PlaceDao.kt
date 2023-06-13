package com.example.diadi.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diadi.domain.Place

@Dao

interface PlaceDao {
    @Query("SELECT * FROM place WHERE place_name = :placeName")
    suspend fun getPlaceByName(placeName: String): Place?

    @Update
    suspend fun updatePlace(place: Place)

    @Insert
    suspend fun insertPlace(place: Place)

    @Query("SELECT * FROM PLACE WHERE PLACE.is_favorite = 1")
    fun findFavoritePlace() : List<Place>
}