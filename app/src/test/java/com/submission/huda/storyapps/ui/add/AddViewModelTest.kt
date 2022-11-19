package com.submission.huda.storyapps.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.model.RegistrasiResponse
import com.submission.huda.storyapps.untils.DataDummy
import com.submission.huda.storyapps.untils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addViewModel: AddViewModel
    private val dummyAddStory = DataDummy.generateDummyAddStory()
    private val dummyErrorAddStory = DataDummy.generateDummyErrorAddStory()
    private val file = DataDummy.dummyFile()
    private val lat = DataDummy.dummyLat()
    private val lon = DataDummy.dummyLon()
    @Before
    fun setUp() {
        addViewModel = AddViewModel(storyRepository)
    }

    @Test
    fun `when Post Add Story and Result Success`() = runTest {
        val expectedAdd = MutableLiveData<Result<RegistrasiResponse>>()
        expectedAdd.value = Result.Success(dummyAddStory)
        val expectedAddResult = AddResult(
            AddView(
                message = dummyAddStory.message!!
            ),
            error = null
        )
        Mockito.`when`(storyRepository.actionAddStory(
            "", lat, lon, "",
            file
        ))
            .thenReturn(expectedAdd.value)
        addViewModel.addStory("", lat, lon, "",
            file
        )
        val actualAddResult = addViewModel.addResult.value
        Mockito.verify(storyRepository).actionAddStory("", lat, lon, "",
            file)
        assertEquals(actualAddResult, expectedAddResult)
    }

    @Test
    fun `when Post Add Story and Result Error`() = runTest {
        val expectedAddEror = MutableLiveData<Result<RegistrasiResponse>>()
        expectedAddEror.value = Result.Success(dummyErrorAddStory)
        val expectedAddResultEror = AddResult(
            error = dummyErrorAddStory.message
        )
        Mockito.`when`(storyRepository.actionAddStory(
            "", lat, lon, "",
            file
        ))
            .thenReturn(expectedAddEror.value)
        addViewModel.addStory("", lat, lon, "",
            file
        )
        val actualAddResultError = addViewModel.addResult.value
        Mockito.verify(storyRepository).actionAddStory("", lat, lon, "",
            file)
        assertEquals(actualAddResultError, expectedAddResultEror)
    }
}