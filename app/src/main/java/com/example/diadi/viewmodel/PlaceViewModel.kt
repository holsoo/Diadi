package com.example.diadi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.diadi.dto.SavePlaceDto
import com.example.diadi.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    suspend fun savePlace(savePlaceDto: SavePlaceDto) : Int{
        val existingPlace = placeRepository.getPlaceByName(savePlaceDto.placeName)

        // 장소가 존재하는 경우: diary Count + 1
        if (existingPlace != null) {
            existingPlace.incrementDiaryCount()
            placeRepository.updatePlace(existingPlace)
            return existingPlace.placeId
        } else {
            return placeRepository.insertPlace(savePlaceDto)
        }
    }
}