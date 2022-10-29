package com.submission.huda.storyapps.ui.login

import com.submission.huda.storyapps.model.LoginResult

data class LoggedInUserView(
    val message: String?,
    val dataSession: LoginResult?
    //... other data fields that may be accessible to the UI
)