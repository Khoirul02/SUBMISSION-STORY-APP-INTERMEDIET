package com.submission.huda.storyapps.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.model.StoryResponse
import com.submission.huda.storyapps.untils.DataDummy
import com.submission.huda.storyapps.untils.getOrAwaitValue
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsActivityViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsActivityViewModel: MapsActivityViewModel
    private val dummyDataListStory  = DataDummy.generateDummyStoryMaps()
    @Before
    fun setUp() {
        mapsActivityViewModel = MapsActivityViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() {
        val expectedStory = MutableLiveData<Result<StoryResponse>>()
        expectedStory.value = Result.Success(dummyDataListStory)
        Mockito.`when`(storyRepository.listStoryLatLong("", 1)).thenReturn(expectedStory)
        val actualStory = mapsActivityViewModel.getAllStoryLatLong("", 1).getOrAwaitValue()
        Mockito.verify(storyRepository).listStoryLatLong("",1)
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Success)
    }

    @Test
    fun `when Get Story Should Return Error`() {
        val expectedStoryErorr = MutableLiveData<Result<StoryResponse>>()
        expectedStoryErorr.value = Result.Error("Error")
        Mockito.`when`(storyRepository.listStoryLatLong("", 1)).thenReturn(expectedStoryErorr)
        val actualStoryError = mapsActivityViewModel.getAllStoryLatLong("", 1).getOrAwaitValue()
        Mockito.verify(storyRepository).listStoryLatLong("",1)
        assertNotNull(actualStoryError)
        assertTrue(actualStoryError is Result.Error)
    }
}