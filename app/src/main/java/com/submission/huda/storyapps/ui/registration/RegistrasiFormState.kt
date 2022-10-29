package com.submission.huda.storyapps.ui.registration

data class RegistrasiFormState(
    val nameError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false)