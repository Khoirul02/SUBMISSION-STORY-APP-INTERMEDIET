package com.submission.huda.storyapps.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.model.LoginResponse
import com.submission.huda.storyapps.untils.DataDummy
import com.submission.huda.storyapps.untils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummmyLoginResponse = DataDummy.generateDummyLogin()
    private val dummmyErorLoginResponse = DataDummy.generateErrorDummyLogin()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Post Login and Result Success`() = runTest {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummmyLoginResponse)
        val expectedLoginResult = LoginResult(
            LoggedInUserView(
                message = dummmyLoginResponse.message,
                dataSession = dummmyLoginResponse.loginResult,
            ),
            error = null
        )
        Mockito.`when`(storyRepository.actionLogin("", ""))
            .thenReturn(expectedLogin.value)
        loginViewModel.login("", "")
        val actualLoginResult = loginViewModel.loginResult.value
        Mockito.verify(storyRepository).actionLogin("", "")
        Assert.assertEquals(actualLoginResult, expectedLoginResult)
    }

    @Test
    fun `when Post Login and Result Error`() = runTest {
        val expectedLoginError = MutableLiveData<Result<LoginResponse>>()
        expectedLoginError.value = Result.Success(dummmyErorLoginResponse)
        val expectedLoginResultError = LoginResult(error = dummmyErorLoginResponse.message)
        Mockito.`when`(storyRepository.actionLogin("", ""))
            .thenReturn(expectedLoginError.value)
        loginViewModel.login("", "")
        val actualLoginResultError = loginViewModel.loginResult.value
        Mockito.verify(storyRepository).actionLogin("", "")
        Assert.assertEquals(actualLoginResultError, expectedLoginResultError)
    }
}