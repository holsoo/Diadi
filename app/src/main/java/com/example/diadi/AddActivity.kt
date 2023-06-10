package com.example.diadi

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.diadi.common.enums.Weathers
import com.example.diadi.databinding.ActivityAddBinding
import com.example.diadi.dto.CreateDiaryDto
import com.example.diadi.viewmodel.DiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileNotFoundException

class AddActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddBinding
    lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    lateinit var diaryViewModel: DiaryViewModel

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

//        val saveDiaryButton = binding.addAddDiaryButton
//        saveDiaryButton.setOnClickListener {
//            onClickAddDiaryButton()
//        }
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

    // 일기 저장 버튼 : 이거 저장버튼 누르면 실행될 수 있게 onCreate에
    // 코드 작성해주시면 됩니다!
    private fun onClickAddDiaryButton(diaryId: Long, title: String, content: String,
                                      imageUrl: String, weather: Weathers, userId: Long, placeId: Long) {
        val createDiaryDto = CreateDiaryDto(
            diaryId = diaryId,
            title = title,
            content = content,
            imageUrl = imageUrl,
            weather = weather,
            userId = userId,
            placeId = placeId
        )

        CoroutineScope(Dispatchers.IO).launch {
            diaryViewModel.createDiary(createDiaryDto)
        }
    }

    // 이미지 화면에 표시
    private fun showImage(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.addAddPicture.setImageBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}
