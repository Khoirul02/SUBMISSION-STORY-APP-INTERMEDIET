package com.submission.huda.storyapps.ui.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.model.RegistrasiResponse
import com.submission.huda.storyapps.untils.DataDummy
import com.submission.huda.storyapps.untils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegistrasiViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registrasiViewModel: RegistrasiViewModel
    private val dummyRegistrasi = DataDummy.generateDummyRegistrasi()
    private val dummyErrorRegistrasi = DataDummy.generateDummyErrorRegistrasi()
    @Before
    fun setUp() {
        registrasiViewModel = RegistrasiViewModel(storyRepository)
    }

    @Test
    fun `when Post Registration and Result Success`() = runTest {
        val expectedRegistration = MutableLiveData<Result<RegistrasiResponse>>()
        expectedRegistration.value = Result.Success(dummyRegistrasi)
        val expectedRegistrationResult = RegistrasiResult(
            RegistrasiView(
                message = dummyRegistrasi.message
            ),
            error = null
        )
        Mockito.`when`(storyRepository.actionRegistration("", "",""))
            .thenReturn(expectedRegistration.value)
        registrasiViewModel.registration("", "", "")
        val actualRegistrationResult = registrasiViewModel.regResult.value
        Mockito.verify(storyRepository).actionRegistration("", "", "")
        assertEquals(actualRegistrationResult, expectedRegistrationResult)
    }

    @Test
    fun `when Post Registration and Result Error`() = runTest {
        val expectedRegistrationError = MutableLiveData<Result<RegistrasiResponse>>()
        expectedRegistrationError.value = Result.Success(dummyErrorRegistrasi)
        val expectedRegistrationResultError = RegistrasiResult(
            error = dummyErrorRegistrasi.message
        )
        Mockito.`when`(storyRepository.actionRegistration("", "",""))
            .thenReturn(expectedRegistrationError.value)
        registrasiViewModel.registration("", "", "")
        val actualRegistrationResultError = registrasiViewModel.regResult.value
        Mockito.verify(storyRepository).actionRegistration("", "", "")
        assertEquals(actualRegistrationResultError, expectedRegistrationResultError)
    }


}