package com.example.diadi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.diadi.databinding.ActivityJoinBinding
import com.example.diadi.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // LiveData : data의 변경을 감지 가능, lifecycle을 알고 있으며 데이터와 UI를 동기화하여 항상 최신 상태의 데이터를 유지할 수 있다.
        // 읽고 확인하시면 이 주석은 지워주세요~
        userViewModel.errorMessage.observe(this) { errorMessage ->
            showErrorMessage(errorMessage)
        }

        val nickname = binding.editTextNickname.toString()
        join(nickname)
    }

    private fun join(nickname : String) {
        CoroutineScope(Dispatchers.IO).launch {
            userViewModel.joinUser(nickname)
        }
    }


    private fun showErrorMessage(message: String) {
        // 메시지를 화면에 표시하는 로직 구현
    }
}