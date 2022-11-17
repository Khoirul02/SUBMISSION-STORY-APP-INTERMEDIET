package com.submission.huda.storyapps.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import kotlinx.coroutines.launch

class RegistrasiViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _regResult = MutableLiveData<RegistrasiResult>()
    val regResult: LiveData<RegistrasiResult> = _regResult
    fun registration(name : String,username: String, password: String){
        viewModelScope.launch {
            val result = storyRepository.actionRegistration(name, username, password)
            if(result is Result.Success){
                if (result.data.error == false) {
                    _regResult.value = RegistrasiResult(success = RegistrasiView(message = result.data.message))
                } else {
                    _regResult.value = RegistrasiResult(error = result.data.message)
                }
            } else {
                _regResult.value = RegistrasiResult(error = "Daftar Akun Gagal!!")
            }
        }
    }
}