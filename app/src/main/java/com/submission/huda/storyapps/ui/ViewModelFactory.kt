package com.submission.huda.storyapps.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.helper.Injection
import com.submission.huda.storyapps.ui.add.AddViewModel
import com.submission.huda.storyapps.ui.dashboard.DashboardViewModel
import com.submission.huda.storyapps.ui.detail.DetailViewModel
import com.submission.huda.storyapps.ui.login.LoginViewModel
import com.submission.huda.storyapps.ui.maps.MapsActivityViewModel
import com.submission.huda.storyapps.ui.registration.RegistrasiViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(RegistrasiViewModel::class.java)) {
            return RegistrasiViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(MapsActivityViewModel::class.java)) {
            return MapsActivityViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}