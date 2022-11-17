package com.submission.huda.storyapps.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.model.ListStoryItem
import kotlinx.coroutines.runBlocking

class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private var mWidgetItems = ArrayList<ListStoryItem>()
//    private lateinit var storyRepository : StoryRepository
    override fun onCreate() {
        fetchData()
    }

    private fun fetchData() {
        runBlocking {
//            storyRepository = StoryRepository()
//            val data = getDataBanner(mContext, storyRepository)
//            if (data is Result.Success){
//                mWidgetItems = data.data.listStory as ArrayList<ListStoryItem>
//            }
            // No Action Data
        }
    }

    override fun onDataSetChanged() {
        fetchData()
    }

    override fun onDestroy() {
    }

    override fun getCount() : Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].photoUrl)
            .apply(RequestOptions() )
            .submit()
            .get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)
        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to mWidgetItems[position].id
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}