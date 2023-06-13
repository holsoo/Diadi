package com.example.diadi.view

import DiaryListAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diadi.databinding.ActivityListBinding
import com.example.diadi.domain.PlaceWithDiaries
import com.example.diadi.viewmodel.DiaryViewModel

class ListActivity : AppCompatActivity() {

    private lateinit var viewModel: DiaryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityListBinding
    private val x = 0.0 // x 값을 초기화하세요
    private val y = 0.0 // y 값을 초기화하세요

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val diaryAdapter = DiaryListAdapter()
        recyclerView.adapter = diaryAdapter

        // DiaryViewModel 생성
        viewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        // 여기서 recycler view에 조회한 데이터를 매핑해주는 역할이 필요하다..!
        // 그리고 일기의 id값도 함께 넘기고, 게시글 상세 조회를 진입하는 경우 해당 일기 id값도 함께 들고 가서
        // 단일 조회가 이루어지도록 하면 좋겠음.
        // 이미지쪽은 처리되는대로 추가해볼게 형..!
    }
}