package com.example.diadi.dto

import com.example.diadi.common.api.meta.SearchMetaData

data class SearchResultPageDto(
    var meta: SearchMetaData,
    var documents : List<SearchResultDto>
)
