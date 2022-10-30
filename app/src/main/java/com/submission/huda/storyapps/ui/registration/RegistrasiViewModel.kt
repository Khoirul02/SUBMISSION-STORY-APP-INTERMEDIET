package com.submission.huda.storyapps.ui.registration

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.model.RegistrasiResponse
import com.submission.huda.storyapps.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrasiViewModel() : ViewModel() {
    private val _regForm = MutableLiveData<RegistrasiFormState>()
    val regFormState: LiveData<RegistrasiFormState> = _regForm

    private val _regResult = MutableLiveData<RegistrasiResult>()
    val regResult: LiveData<RegistrasiResult> = _regResult
    fun reg(name : String,username: String, password: String) {
        RetrofitClient.instance.regUser(name, username, password)
            .enqueue(object : Callback<RegistrasiResponse>{
                override fun onResponse(
                    call: Call<RegistrasiResponse>?,
                    response: Response<RegistrasiResponse>?
                ) {
                    val result = response!!.body()
                    if (result !== null){
                        if (result.error == false) {
                            _regResult.value = RegistrasiResult(success = RegistrasiView(message = result.message))
                        } else {
                            _regResult.value = RegistrasiResult(error = result.message)
                        }
                    } else {
                        _regResult.value = RegistrasiResult(error = "Daftar Akun Gagal")
                    }
                }

                override fun onFailure(call: Call<RegistrasiResponse>?, t: Throwable?) {
                    _regResult.value = RegistrasiResult(error = "Daftar Akun Gagal")
                }

            })
    }
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _regForm.value = RegistrasiFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _regForm.value = RegistrasiFormState(passwordError = R.string.invalid_password)
        } else {
            _regForm.value = RegistrasiFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()

    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}