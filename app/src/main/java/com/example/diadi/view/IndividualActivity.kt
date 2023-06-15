package com.example.diadi.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.diadi.R
import com.example.diadi.databinding.ActivityIndividualBinding
import com.example.diadi.viewmodel.DiaryViewModel

class IndividualActivity : AppCompatActivity() {
    lateinit var diaryViewModel : DiaryViewModel
    lateinit var binding: com.example.diadi.databinding.ActivityIndividualBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val modifyButton: Button = findViewById(R.id.ind_modify)
        val deleteButton: Button = findViewById(R.id.ind_delete)

        modifyButton.setOnClickListener {
            val modifyIntent = Intent(this, ModifyActivity::class.java)
            startActivity(modifyIntent)
        }

        val id = intent.getStringExtra("id")
        val imageUrl = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        val date = intent.getStringExtra("date")
        val title = intent.getStringExtra("title")
        val address = intent.getStringExtra("address")
        val order = intent.getStringExtra("order")

        deleteButton.setOnClickListener {
            // 여기에 일기 삭제 로직을 구현하세요.
            // 삭제 후 필요한 작업을 수행하세요.
            if (id != null) {
                diaryViewModel.deleteDiary(id.toLong())
            }
            val toMainIntent = Intent(this, MainActivity::class.java)
            startActivity(toMainIntent)
        }

        var indOrder : TextView = findViewById(R.id.ind_order)
        indOrder.text = order
        var indDate : TextView = findViewById(R.id.ind_date)
        indDate.text = date
        var indImage : ImageView = findViewById(R.id.ind_myPicture)
        Glide.with(this).load(imageUrl).into(indImage)
        var indName : TextView = findViewById(R.id.ind_location)
        indName.text = name.toString()
        var indTitle : TextView = findViewById(R.id.ind_diaryTitle)
        indTitle.text = title.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menu?.add(0,1,0,"수정하기")
        menu?.add(0,2,0,"삭제하기")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_modify -> {
                val modifyIntent = Intent(this, ModifyActivity::class.java)
                startActivity(modifyIntent)
                return true
            }
            R.id.menu_delete -> {
                // 여기에 일기 삭제 로직을 구현하세요.
                // 삭제 후 필요한 작업을 수행하세요.
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
