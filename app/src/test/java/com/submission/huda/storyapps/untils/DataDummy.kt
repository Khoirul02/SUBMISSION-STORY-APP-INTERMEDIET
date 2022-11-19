package com.submission.huda.storyapps.untils
import com.submission.huda.storyapps.helper.timeStamp
import com.submission.huda.storyapps.model.*
import java.io.File

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

    fun generateDummyStoryMaps(): StoryResponse {
        val listStory: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1668845671790_SSt6rByS.jpg",
                "2022-11-19T08:14:31.793Z",
                "Title $i",
                "Description $i",
                110.39119753197345,
                "$i",
                -7.801801801801802

            )
            listStory.add(story)
        }
        return StoryResponse(
            listStory,
            false,
            "Story fetched successfully",
        )
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

    fun generateDummyLogin(): LoginResponse {
        return LoginResponse(
            LoginResult("", "", ""),
            false,
            "success",
        )
    }
    fun generateErrorDummyLogin(): LoginResponse {
        return LoginResponse(
            null,
            true,
            "Invalid password",
        )
    }

    fun generateDummyRegistrasi(): RegistrasiResponse {
        return RegistrasiResponse(
            false,
            "User Created",
        )
    }

    fun generateDummyErrorRegistrasi(): RegistrasiResponse {
        return RegistrasiResponse(
            true,
            "User Created Failed",
        )
    }

    fun generateDummyAddStory(): RegistrasiResponse {
        return RegistrasiResponse(
            false,
            "success",
        )
    }

    fun dummyFile(): File {
        return File.createTempFile(timeStamp, ".jpg", null)
    }

    fun dummyLat() : Float {
        return (-7.801801801801802).toFloat()
    }

    fun dummyLon() : Float {
        return (110.39119753197345).toFloat()
    }

    fun generateDummyErrorAddStory(): RegistrasiResponse {
        return RegistrasiResponse(
            true,
            "Failed",
        )
    }
}