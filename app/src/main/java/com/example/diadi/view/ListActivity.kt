package com.example.diadi.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diadi.databinding.ActivityListBinding
import com.example.diadi.domain.PlaceWithDiaries
import com.example.diadi.view.recycler.adapter.DiaryListAdapter
import com.example.diadi.view.recycler.item.ListLayout
import com.example.diadi.viewmodel.DiaryViewModel

class ListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListBinding

    private lateinit var diaryViewModel: DiaryViewModel
    private val listItems = arrayListOf<ListLayout>()
    private val diaryListAdapter = DiaryListAdapter(listItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = diaryListAdapter

        // 사용자가 키워드 검색 후 리스트 아이템 클릭하면 발생하는 이벤트!
        diaryListAdapter.setItemClickListener(object: DiaryListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(this@ListActivity, AddActivity::class.java)
                intent.putExtra("imageUrl", listItems[position].imageUrl)
                intent.putExtra("name", listItems[position].placeName)
                intent.putExtra("date", listItems[position].date)
                intent.putExtra("title", listItems[position].title)
                intent.putExtra("address", listItems[position].content)
                intent.putExtra("order", position)
            }
        })
    }

    private fun showDiaryResult(placeName: String, x : Double, y :Double) {

        // 1. POI Item 리스너 추가가 필요함.
        // 2. 해당 리스너에서 ListActivity.kt로 옮겨오면서, 그 좌표의 장소명(place name), x, y 값을 받아온다.
        // 3. 저기 placeName, x, y에 잘 넣어준다.
        val results: PlaceWithDiaries = diaryViewModel.getPlaceWithDiaries(x, y)
        if (results != null) {
            for (result in results.diaries) {
                val item = ListLayout(
                    diaryId = result.diaryId,
                    imageUrl = result.imageUrl,
                    placeId = result.placeId,
                    placeName = placeName,
                    title = result.title,
                    content = result.content,
                    date = result.createdAt.toString()
                )

                listItems.add(item)
            }
            diaryListAdapter.notifyDataSetChanged()
        }
    }
}
