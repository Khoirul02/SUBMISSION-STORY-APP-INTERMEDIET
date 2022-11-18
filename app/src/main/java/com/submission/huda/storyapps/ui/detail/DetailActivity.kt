package com.submission.huda.storyapps.ui.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.databinding.ActivityDetailBinding
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.helper.formatedDate
import com.submission.huda.storyapps.ui.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.detail)
        val image = binding.ivDetailPhoto
        val title = binding.tvDetailName
        val description = binding.tvDetailDescription
        val date = binding.tvItemDetailDate
        val progressBar = binding.progressBarDetail
        image.visibility = View.GONE
        title.visibility = View.GONE
        description.visibility = View.GONE
        date.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        val id = intent.extras!!.getString("ID_STORY")
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPreferences.getString(Config.TOKEN,"")
        detailViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[DetailViewModel::class.java]
        id?.let { detailViewModel.getDetail(token, it) }
        detailViewModel.getDetail(token, id.toString()).observe(this, Observer {
            val detailResult = it ?:return@Observer
            when (detailResult) {
                is Result.Success -> {
                    if (detailResult.data.error == true) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext, detailResult.data.message, Toast.LENGTH_LONG).show()
                    } else {
                        progressBar.visibility = View.GONE
                        image.visibility = View.VISIBLE
                        title.visibility = View.VISIBLE
                        description.visibility = View.VISIBLE
                        date.visibility = View.VISIBLE
                        val result = detailResult.data.story
                        val createAt = formatedDate(result?.createdAt!!)
                        Glide.with(this)
                            .load(result.photoUrl)
                            .apply(RequestOptions().override(512, 512))
                            .into(image)
                        title.text = result.name
                        description.text = result.description
                        date.text = createAt
                    }
                }
                is Result.Error -> {
                    Toast.makeText(applicationContext, detailResult.exception, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
