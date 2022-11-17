package com.submission.huda.storyapps.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.submission.huda.storyapps.data.StoryRepository

class StackWidgetService() : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
    }