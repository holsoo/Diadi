package com.example.diadi.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.diadi.R
import com.example.diadi.databinding.ActivityIndividualBinding

class IndividualActivity : AppCompatActivity() {
    lateinit var binding : ActivityIndividualBinding

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

        }
    }
}