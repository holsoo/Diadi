package com.example.diadi.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.diadi.databinding.ActivityModifyBinding

class ModifyActivity : AppCompatActivity() {
    lateinit var binding : ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}