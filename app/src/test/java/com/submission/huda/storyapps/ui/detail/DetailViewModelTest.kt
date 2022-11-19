package com.submission.huda.storyapps.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.model.DetailResponse
import com.submission.huda.storyapps.untils.DataDummy
import com.submission.huda.storyapps.untils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var detailViewModel: DetailViewModel
    private val dummyDetailStory = DataDummy.generateDummyDetailStory()
    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(storyRepository)
    }

    @Test
    fun `when Get Detail Story Should Not Null and Return Success`() {
        val expectedDetailStory = MutableLiveData<Result<DetailResponse>>()
        expectedDetailStory.value = Result.Success(dummyDetailStory)
        `when`(storyRepository.actionDetailStory("", "")).thenReturn(expectedDetailStory)
        val actualDetailStory = detailViewModel.getDetail("", "").getOrAwaitValue()
        Mockito.verify(storyRepository).actionDetailStory("","")
        Assert.assertNotNull(actualDetailStory)
        Assert.assertTrue(actualDetailStory is Result.Success)
    }

    @Test
    fun `when Get Detail Story Should and Return Error`() {
        val expectedDetailStoryError = MutableLiveData<Result<DetailResponse>>()
        expectedDetailStoryError.value = Result.Error("Error")
        `when`(storyRepository.actionDetailStory("", "")).thenReturn(expectedDetailStoryError)
        val actualDetailStoryError = detailViewModel.getDetail("", "").getOrAwaitValue()
        Mockito.verify(storyRepository).actionDetailStory("","")
        Assert.assertNotNull(actualDetailStoryError)
        Assert.assertTrue(actualDetailStoryError is Result.Error)
    }
}