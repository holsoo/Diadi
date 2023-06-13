package com.example.diadi.dto

data class ShowDiaryDto(
    val id : Long,
    val imageUrl : String,
    val placeName : String,
    val title : String,
    val content : String,
    val date : String
)
