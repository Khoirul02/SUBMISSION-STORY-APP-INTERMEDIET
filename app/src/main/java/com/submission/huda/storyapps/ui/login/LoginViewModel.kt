package com.submission.huda.storyapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.model.LoginResponse
import com.submission.huda.storyapps.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        RetrofitClient.instance.loginUser(
            username,password
        ).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>?,
                response: Response<LoginResponse>?
            ) {
                val result = response!!.body()
                if (result !== null){
                    if (result.error == false) {
                        _loginResult.value = LoginResult(success = LoggedInUserView(message = result.message, dataSession = result.loginResult))
                    } else {
                        _loginResult.value = LoginResult(error = result.message)
                    }
                } else {
                    _loginResult.value = LoginResult(error = "Login Gagal")
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                _loginResult.value = LoginResult(error = "Login Gagal")
            }
        })
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}