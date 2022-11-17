package com.submission.huda.storyapps.ui.login

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: String? = null
)