package com.example.diadi.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.diadi.R
import com.example.diadi.databinding.ActivityIndividualBinding

class IndividualActivity : AppCompatActivity() {

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

        deleteButton.setOnClickListener {
            // 여기에 일기 삭제 로직을 구현하세요.
            // 삭제 후 필요한 작업을 수행하세요.
        }
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
