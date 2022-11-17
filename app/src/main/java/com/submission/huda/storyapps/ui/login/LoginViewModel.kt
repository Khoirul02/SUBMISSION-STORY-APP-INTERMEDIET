package com.submission.huda.storyapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = storyRepository.actionLogin(username, password)
            if (result is Result.Success) {
                if (result.data.error == false) {
                    _loginResult.value = LoginResult(
                        success = LoggedInUserView(
                            message = result.data.message,
                            dataSession = result.data.loginResult
                        )
                    )
                } else {
                    _loginResult.value = LoginResult(error = result.data.message)
                }
            } else {
                _loginResult.value = LoginResult(error = "Gagal Login!!")
            }
        }
    }
}