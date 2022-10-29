package com.submission.huda.storyapps.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.model.ListStoryItem
import com.submission.huda.storyapps.ui.detail.DetailActivity

@SuppressLint("NotifyDataSetChanged")
class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    private val mData = ArrayList<ListStoryItem>()
    fun setData(items: ArrayList<ListStoryItem>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_data, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val photo : ImageView = itemView.findViewById(R.id.iv_item_photo)
        val name : TextView = itemView.findViewById(R.id.tv_item_name)
        val description : TextView = itemView.findViewById(R.id.tv_item_description)
        fun bind (story: ListStoryItem){
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(RequestOptions().override(512, 512))
                .into(photo)
            name.text = story.name
            description.text = story.description
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("ID_STORY", story.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}