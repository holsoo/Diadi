package com.example.diadi.common.api

import com.example.diadi.dto.SearchResultPageDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {

    @GET("v2/local/search/keyword.json")
    fun getSearchKeyword (
        @Header("Authorization") key : String,
        @Query("query") query : String,
        @Query("page") page : Int
    ): Call<SearchResultPageDto>
}