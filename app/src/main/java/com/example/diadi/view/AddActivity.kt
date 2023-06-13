package com.example.diadi.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.widget.Button

import android.widget.Toast

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import coil.load

import com.example.diadi.R
import com.example.diadi.common.enums.Weathers
import com.example.diadi.databinding.ActivityAddBinding
import com.example.diadi.dto.CreateDiaryDto

import com.example.diadi.dto.SavePlaceDto
import com.example.diadi.dto.SearchResultDto
import com.example.diadi.viewmodel.DiaryViewModel
import com.example.diadi.viewmodel.PlaceViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    private lateinit var launcher: ActivityResultLauncher<Intent>

    lateinit var binding : ActivityAddBinding
    lateinit var diaryViewModel: DiaryViewModel
    lateinit var placeViewModel: PlaceViewModel

    lateinit var searchResultDto: SearchResultDto

    lateinit var diaryImageUri : Uri
    lateinit var diaryWeathers : Weathers

    private val PICK_IMAGE_FROM_GALLERY = 1000
    private val PICK_IMAGE_FROM_GALLERY_PERMISSION = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addPictureButton.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> showGallery(this@AddActivity)

                // 갤러리 접근 권한이 없는 경우 && 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                -> showPermissionContextPopup()

                // 권한 요청 하기
                else -> requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_FROM_GALLERY_PERMISSION)
            }
        }

        diaryWeather()
        diaryViewModel = ViewModelProvider(this)[DiaryViewModel::class.java]

        val searchEditText = binding.addAddPlace
        searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

            searchResultDto.place_name = intent.getStringExtra("name").toString()
            searchResultDto.category_group_name = intent.getStringExtra("category").toString()
            searchResultDto.road_address_name = intent.getStringExtra("address").toString()
            searchResultDto.x = intent.getDoubleExtra("x", 0.0)
            searchResultDto.y = intent.getDoubleExtra("y", 0.0)
        }

        val saveDiaryButton = binding.addAddDiaryButton
        saveDiaryButton.setOnClickListener {
            onClickAddDiaryButton(
                binding.addAddTitle.toString(),
                binding.addAddDiaryContent.toString(),
                binding.addAddDate.toString()
            )
        }

        val addPictureButton = binding.addPictureButton
        addPictureButton.setOnClickListener {
            val intent = Intent().also { intent ->
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT
            }
            launcher.launch(intent)
        }
        // 1-5 저장 버튼 끝나면 저장 완료되었을테고 이제 마커를 추가하는 코드 작성하면 될듯..?
    }

    private fun showGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY)
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PICK_IMAGE_FROM_GALLERY_PERMISSION)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    // 사진 선택(갤러리에서 나온) 이후 실행되는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK)
            data?.let { binding.addAddPicture.load(it.data) }
    }

    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            PICK_IMAGE_FROM_GALLERY_PERMISSION ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showGallery(this@AddActivity)
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    // 일기 저장 - Place 저장, Diary 저장.
    private fun onClickAddDiaryButton (
        title: String,
        content: String,
        date : String
    ) {
        val savePlaceDto = SavePlaceDto(
            placeName = searchResultDto.place_name,
            category = searchResultDto.category_group_name,
            address = searchResultDto.road_address_name,
            x = searchResultDto.x,
            y = searchResultDto.y
        )

        CoroutineScope(Dispatchers.IO).launch {
            // 장소 등록
            val placeId = placeViewModel.savePlace(savePlaceDto)
            diaryWeathers = Weathers.ETC

            // 일기 등록
            val createDiaryDto = CreateDiaryDto(
                title = title,
                content = content,
                imageUrl = diaryImageUri.toString(),
                weather = diaryWeathers,
                date = date,
                placeId = placeId
            )

            diaryViewModel.createDiary(createDiaryDto)
        }
    }

    // 선택된 날씨 가져오기
    private var selectedButton: Button? = null
    private fun diaryWeather() {

        val sunnyBtn: Button = findViewById(R.id.add_sunny)
        val cloudyBtn: Button = findViewById(R.id.add_cloudy)
        val rainyBtn: Button = findViewById(R.id.add_rainy)
        val snowyBtn: Button = findViewById(R.id.add_snowy)

        sunnyBtn.setOnClickListener { selectButton(sunnyBtn, Weathers.SUNNY) }
        cloudyBtn.setOnClickListener { selectButton(cloudyBtn, Weathers.CLOUDY) }
        rainyBtn.setOnClickListener { selectButton(rainyBtn, Weathers.RAINY) }
        snowyBtn.setOnClickListener { selectButton(snowyBtn, Weathers.SNOWY) }
    }

    // weather 버튼 선택
    private fun selectButton(button: Button, weather: Weathers) {
        selectedButton?.let {
            it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.transparent))
        }
        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.ddddff))
        selectedButton = button

        diaryWeathers = weather
    }
}