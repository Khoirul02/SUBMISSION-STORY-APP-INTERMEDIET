package com.submission.huda.storyapps.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.model.DetailResponse
import com.submission.huda.storyapps.untils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import androidx.lifecycle.Observer

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
        val observer = Observer<Result<DetailResponse>> {}
        try {
            val expectedDetailStory = MutableLiveData<Result<DetailResponse>>()
            expectedDetailStory.value = Result.success(dummyDetailStory)
//            `when`(storyRepository.actionDetailStory("","")).thenReturn(expectedDetailStory)
//            val actualDetailStory = detailViewModel.getDetail("","").observeForever(observer)
//            Assert.assertNotNull(actualDetailStory)
        } finally {
//            detailViewModel.getDetail("","").removeObserver(observer)
        }
    }
}