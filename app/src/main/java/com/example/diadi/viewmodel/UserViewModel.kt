package com.example.diadi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diadi.repository.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    fun joinUser (nickname: String) {
        viewModelScope.launch{
            userRepository.insertUser(nickname)
        }
    }
}