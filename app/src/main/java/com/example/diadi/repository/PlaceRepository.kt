package com.example.diadi.repository

import com.example.diadi.dao.PlaceDao
import com.example.diadi.domain.Place
import com.example.diadi.dto.SavePlaceDto
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val placeDao: PlaceDao) {

    suspend fun getPlaceByName(placeName: String): Place? {
        return placeDao.getPlaceByName(placeName)
    }

    suspend fun insertPlace(savePlaceDto: SavePlaceDto) : Int {
        var place = savePlaceDto.toEntity()
        placeDao.insertPlace(place)
        return place.placeId
    }

    suspend fun updatePlace(place: Place) {
        placeDao.updatePlace(place)
    }
}