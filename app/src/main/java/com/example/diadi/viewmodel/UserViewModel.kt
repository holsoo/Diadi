package com.example.diadi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diadi.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel (private val userRepository : UserRepository) : ViewModel() {

    fun joinUser (nickname: String) {
        viewModelScope.launch{
            userRepository.insertUser(nickname)
        }
    }
}