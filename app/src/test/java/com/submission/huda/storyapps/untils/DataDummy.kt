package com.submission.huda.storyapps.untils

import androidx.lifecycle.LiveData
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.model.DetailResponse
import com.submission.huda.storyapps.model.ListStoryItem
import com.submission.huda.storyapps.model.Story

object DataDummy {
    fun generateDummyStory(): List<ListStoryItem> {
        val listStory: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1668845671790_SSt6rByS.jpg",
                "2022-11-19T08:14:31.793Z",
                "Title $i",
                "Description $i",
                "null",
                "$i",
                "null"
            )
            listStory.add(story)
        }
        return listStory
    }

    fun generateDummyDetailStory(): DetailResponse {
        val story = Story(
            "https://story-api.dicoding.dev/images/stories/photos-1668847485660_czkpsmx9.jpg",
            "2022-11-19T08:44:45.661Z",
            "Dummy Name",
            "Dummy Description",
            null,
            "story-id",
            null
        )
        return DetailResponse(
            false,
            "Story fetched successfully",
            story = story,
        )
    }
}