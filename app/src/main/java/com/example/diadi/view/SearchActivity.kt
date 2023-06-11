package com.example.diadi.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diadi.common.api.KakaoAPI
import com.example.diadi.databinding.ActivitySearchBinding
import com.example.diadi.dto.SearchResultDto
import com.example.diadi.dto.SearchResultPageDto
import com.example.diadi.view.recycler.adapter.SearchAdapter
import com.example.diadi.view.recycler.item.SearchLayout
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding

    companion object {
        const val BASE_URL = "https://dapi.kakao.com"
        const val API_KEY = "KakaoAK dde449b6aff5714512423998244dfe76"
    }

    private val searchItems = arrayListOf<SearchLayout>()
    private val searchAdapter = SearchAdapter(searchItems)
    private var pageNumber = 1
    private var keyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = searchAdapter

        // 검색 후 item 클릭시.
        searchAdapter.setItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val mapPoint = MapPoint.mapPointWithGeoCoord(searchItems[position].y, searchItems[position].x)
                binding.mapView.setMapCenterPointAndZoomLevel(mapPoint, 2, true)
            }
        })

        // 검색
        binding.btnSearch.setOnClickListener {
            keyword = binding.input.text.toString()
            pageNumber = 1
            search(keyword, pageNumber)
        }

        // 검색 결과 조회 : 이전 페이지
        binding.btnPrevPage.setOnClickListener {
            pageNumber--
            binding.tvPageNumber.text = pageNumber.toString()
            search(keyword, pageNumber)
        }

        // 검색 결과 조회 : 다음 페이지
        binding.btnNextPage.setOnClickListener {
            pageNumber++
            binding.tvPageNumber.text = pageNumber.toString()
            search(keyword, pageNumber)
        }
    }


    private fun search(keyword : String, page : Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(KakaoAPI::class.java)
        val call = api.getSearchKeyword(API_KEY, keyword, page)

        // 서버에 Request
        call.enqueue(object : Callback<SearchResultPageDto> {

            // 200 OK
            override fun onResponse(
                call: Call<SearchResultPageDto>,
                response: Response<SearchResultPageDto>
            ) {
                showSearchResult(response.body())
            }

            // 40x Failure
            override fun onFailure(call: Call<SearchResultPageDto>, t: Throwable) {
                Log.w("Search", "통신 실패 : ${t.message}")
            }
        }
        )
    }

    private fun showSearchResult(searchResult : SearchResultPageDto?) {
        if(!searchResult?.documents.isNullOrEmpty()) {
            // 검색 결과 x
            searchItems.clear()
            binding.mapView.removeAllPOIItems()

            for (document in searchResult!!.documents) {
                val item = SearchLayout(
                    document.place_name,
                    document.category_group_name,
                    document.road_address_name,
                    document.x.toDouble(),
                    document.y.toDouble()
                )

                searchItems.add(item)

                addMarkerOnMap(document)
            }
            searchAdapter.notifyDataSetChanged()

            binding.btnNextPage.isEnabled = !searchResult.meta.is_end
            binding.btnPrevPage.isEnabled = pageNumber != 1

        } else {
            Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addMarkerOnMap(document: SearchResultDto) {
        val point = MapPOIItem()
        point.apply {
            itemName = document.place_name
            mapPoint = MapPoint.mapPointWithGeoCoord(document.y.toDouble(), document.x.toDouble())
            markerType = MapPOIItem.MarkerType.RedPin
            selectedMarkerType = MapPOIItem.MarkerType.BluePin
        }

        binding.mapView.addPOIItem(point)
    }
}
