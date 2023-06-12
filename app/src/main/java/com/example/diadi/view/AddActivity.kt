package com.example.diadi.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import android.widget.Toast

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
import java.io.FileNotFoundException

class AddActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddBinding
    lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    lateinit var diaryViewModel: DiaryViewModel

    lateinit var placeViewModel: PlaceViewModel
    lateinit var searchResultDto: SearchResultDto
    lateinit var diaryImageUri : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        diaryViewModel = ViewModelProvider(this)[DiaryViewModel::class.java]

        initGallery()

        val galleryButton = binding.addAddPictureButton
        galleryButton.setOnClickListener {
            onClickGalleryButton()
        }

        // 일단 xml상에서 장소 관련 (editText)를 클릭하면, 장소를 찾아주는 searchActivity로 이동하도록 해야 함.
        // 그리고 return값으로 SearchResultDto를 받아올거야 

        val searchEditText = binding.addAddPlace
        searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

            val receiveIntent = intent
            val y = intent.getDoubleExtra("y", 0.0)
            val x = intent.getDoubleExtra("x", 0.0)
            val name = intent.getStringExtra("name")
            val category = intent.getStringExtra("category")
            val address = intent.getStringExtra("address")

            searchResultDto.x = x
            searchResultDto.y = y
            searchResultDto.place_name = name
            searchResultDto.category_group_name = category
            searchResultDto.road_address_name = address

            // SearchActivity.kt로 페이지 이동
            // 동혁이형이 해야 할것.
            // 1-1 searchActivity로 이동
            // 장소를 찾아주는 searchActivity.kt로 페이지 이동하도록 해야 함 (완료)

            // 1-3의 반환값을 받는다. 반환값은 아래와 같다. (완료)
            // searchResultDto = 아까 반환한 searchItems[pointer] 를 lateinit으로 선언된 이 변수에 저장.
            // y와 x 옆의 0.0은 전달된 값이 없을경우 기본값
            // searchResultDto에서 x, y 타입 Double로 변경하고 나머지는 string? 으로 변경
        }

        // 이미지 저장 리스너 코드 등록
        val addImage : Button = findViewById(R.id.add_addPictureButton)
        var inputImage: ImageView = findViewById(R.id.add_addPicture)

        addImage.setOnClickListener {
            onClickGalleryButton()
            // 갤러리 열고
            initGallery()
            // diaryImageUri 에 Uri 저장 됐으니 이거를 ImageView에 넘겨주면
            if (diaryImageUri != null) {
                val uri = Uri.parse(diaryImageUri.toString())
                inputImage.setImageURI(uri)
            }
            // ImageView로 해당 이미지 넘겨주기 완료
        }

        val saveDiaryButton = binding.addAddDiaryButton
        saveDiaryButton.setOnClickListener {
            val diaryImage: ImageView = findViewById(R.id.add_addPicture)
            val diaryTitle: EditText = findViewById(R.id.add_addTitle)
            val diaryDate: EditText = findViewById(R.id.add_addDate)
            val diaryPlace: EditText = findViewById(R.id.add_addPlace)
//        1-4 여기에 리스너 위의 두줄처럼 달고, onClickAddDiaryButton 파라미터 담아서 실행시켜줘
            //onClickAddDiaryButton(
                //title: String,
                //content: String,
                //imageUrl: String,
                //weather: Weathers
                // 여기를 어떻게 해야할지 모르겠네??????
                // 일단 diaryWeather() 하면 선택된 버튼의 날씨 호출 될거야
              //)
        }
        // 저 파라미터는 유저가 작성 화면에서 입력한 editText.text값이야 그냥 binding해서 넣기.

        // 1-5 저장 버튼 끝나면 저장 완료되었을테고 이제 마커를 추가하는 코드 작성하면 될듯..?
        // 마커 추가는 SearchActivity의 addMarkerOnMap(document: SearchResultDto) 참고해도 좋슴다
        
        // 이게 연결해야 테스트 가능해서 일단 테스트가 당장은 어려워가지고 
        // 실행해서 에러가 나더라도 일단 페이지간 이동이랑 연결작업이 마저 끝나야 다음 단계로 갈 수 있어..
        // 에러 여부와 상관 없이 일단 형 몫은 완성해주라!
        // 이제 형님 역할이 중요합니다.. 여기서 안되면 완성 힘들어질수도 있어
    }

    // 갤러리 호출, 이미지를 선택하면 이미지의 uri를 가져온다.
    private fun initGallery() {
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val image: Intent? = result.data
                if (image != null) {
                    val imageUrl = image.data
                    if (imageUrl != null) {
                        showImage(imageUrl)
                    }
                }
            }
        }
    }

    // 갤러리로 들어가는 버튼
    private fun onClickGalleryButton() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        galleryLauncher.launch(intent)
    }

    // 일기 저장 - Place 저장, Diary 저장.
    private fun onClickAddDiaryButton(
        title: String,
        content: String,
        imageUrl: String,
        weather: Weathers
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
            if (imageUrl != null && savePlaceDto.placeName != null) {
                val placeId = placeViewModel.savePlace(savePlaceDto)

                // 일기 등록
                val createDiaryDto = CreateDiaryDto(
                    title = title,
                    content = content,
                    imageUrl = imageUrl,
                    weather = weather,
                    placeId = placeId
                )
                diaryViewModel.createDiary(createDiaryDto)
            } else {
                Toast.makeText(applicationContext, "이미지나 장소가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // 이미지 화면에 표시
    private fun showImage(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.addAddPicture.setImageBitmap(bitmap)

            diaryImageUri = uri; // db에 저장하기 위한 image uri

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    // 선택된 날씨 가져오기
    private var selectedButton: Button? = null
    private var selectedValue: String? = null
    private fun diaryWeather() {

        val sunnyBtn: Button = findViewById(R.id.add_sunny)
        val cloudyBtn: Button = findViewById(R.id.add_cloudy)
        val rainyBtn: Button = findViewById(R.id.add_rainy)
        val snowyBtn: Button = findViewById(R.id.add_snowy)

        sunnyBtn.setOnClickListener { selectButton(sunnyBtn, Weathers.SUNNY.toString()) }
        cloudyBtn.setOnClickListener { selectButton(cloudyBtn, Weathers.CLOUDY.toString()) }
        rainyBtn.setOnClickListener { selectButton(rainyBtn, Weathers.RAINY.toString()) }
        snowyBtn.setOnClickListener { selectButton(snowyBtn, Weathers.SNOWY.toString()) }
    }

    // 버튼 선택
    private fun selectButton(button: Button, value: String): String? {
        selectedButton?.let {
            it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.transparent))
        }
        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.ddddff))
        selectedButton = button
        selectedValue = value

        return selectedValue
    }
}
