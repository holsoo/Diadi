package com.example.diadi.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diadi.domain.Place

@Dao
interface PlaceDao {

    @Insert
    fun insertPlace(place: Place)

    @Update
    fun updatePlace(place: Place)

    @Delete
    fun deletePlace(place: Place)

    @Query("SELECT * FROM PLACE WHERE PLACE.is_favorite = 1")
    fun findFavoritePlace()
}