package com.example.diadi.dto

data class SearchResultDto(
    var id : String,
    var place_name : String,
    var category_group_name : String,
    var road_address_name : String,
    var x : String, // logitude
    var y : String, // latitude
    var distanc: String
)
