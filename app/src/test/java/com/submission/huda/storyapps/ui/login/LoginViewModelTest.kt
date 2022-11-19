package com.submission.huda.storyapps.ui.login

import com.submission.huda.storyapps.data.StoryRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyUserEmail = "hudak006@gmail.com"
    private val dummyUserPassword = "hudak006@gmail.com"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Post Login and Return Success`() {
        val expectedLogin = ""
    }
}