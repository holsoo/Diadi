package com.example.diadi

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.diadi.databinding.ActivityAddBinding
import java.io.FileNotFoundException

class AddActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddBinding
    lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val galleryButton = binding.addAddPictureButton
        galleryButton.setOnClickListener {
            onClickGalleryButton()
        }

        // 갤러리 호출, 이미지를 선택하면 이미지의 uri를 가져온다.
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