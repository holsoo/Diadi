package com.example.diadi.dto

import com.example.diadi.domain.Place

data class SavePlaceDto(
    val placeName : String?,
    val category : String?,
    val address : String?,
    val x : Double,
    val y : Double
) {
    fun toEntity(): Place {
        return Place(
            placeName = placeName,
            category = category,
            address = address,
            x = x,
            y = y,
            isFavorite = false,
        )
    }
}