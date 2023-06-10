package com.example.diadi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diadi.repository.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val NICKNAME_LENGTH_ERROR = "닉네임은 4~20자 길이로 작성되어야 합니다."
    private val NICKNAME_DUPLICATION_ERROR = "중복된 닉네임입니다."
    private val error = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = error

    suspend fun joinUser (nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            validateNickname(nickname)
            userRepository.insertUser(nickname)
        }
    }

    private fun validateNickname(nickname: String) {
        validateFormat(nickname)
        validateDuplicateNickname(nickname)
    }

    private fun validateFormat(nickname: String) {
        if (nickname.isEmpty() || nickname.length < 4 || nickname.length > 20) {
            error.postValue(NICKNAME_LENGTH_ERROR)
            return
        }
    }

    private fun validateDuplicateNickname(nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (checkDuplicate(nickname)) {
                error.postValue(NICKNAME_DUPLICATION_ERROR)
            }
        }
    }

    private fun checkDuplicate(nickname: String): Boolean {
        return userRepository.isNicknameExists(nickname)
    }
}