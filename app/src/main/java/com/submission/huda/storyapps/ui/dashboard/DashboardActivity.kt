package com.submission.huda.storyapps.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.adapter.ListStoryAdapter
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.databinding.ActivityDashboardBinding
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.model.ListStoryItem
import com.submission.huda.storyapps.model.StoryResponse
import com.submission.huda.storyapps.ui.ViewModelFactory
import com.submission.huda.storyapps.ui.add.AddActivity
import com.submission.huda.storyapps.ui.login.LoginActivity
import com.submission.huda.storyapps.ui.maps.MapsActivity

@Suppress("UNCHECKED_CAST")
@SuppressLint("NotifyDataSetChanged")
class DashboardActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var adapter: ListStoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val progressBar = binding.progressBar
        val rvStory = binding.rvStory
        val add = binding.add
        progressBar.visibility = View.VISIBLE
        adapter = ListStoryAdapter()
        adapter.notifyDataSetChanged()
        rvStory.layoutManager = LinearLayoutManager(applicationContext)
        rvStory.adapter = adapter
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPreferences.getString(Config.TOKEN,"")
        dashboardViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[DashboardViewModel::class.java]
        dashboardViewModel.getAllStory(token, 1, 10).observe(this, Observer {
            val storyResult = it ?:return@Observer
            when (storyResult) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    if (storyResult.data.error == true) {
                        Toast.makeText(applicationContext, storyResult.data.message, Toast.LENGTH_LONG).show()
                    } else {
                        progressBar.visibility = View.GONE
                        viewContentStory(storyResult.data)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(applicationContext, storyResult.exception, Toast.LENGTH_LONG).show()
                }
            }
        })
        getData(token, 1,10)
        add.setOnClickListener {
            val intent = Intent(applicationContext, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getData(token : String, page : Int, size : Int) {
        dashboardViewModel.getAllStory(token, page, size)
    }

    private fun viewContentStory(data: StoryResponse) {
        if(data.listStory!!.isNotEmpty()) {
            adapter.setData(data.listStory as ArrayList<ListStoryItem>)
        } else {
            binding.ivEmpty.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.VISIBLE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                onLogout(applicationContext)
            }
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_maps -> {
                val mIntent = Intent(applicationContext, MapsActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onLogout(context: Context) {
        val sharedPreferences = context.getSharedPreferences(Config.SHARED_PRED_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Config.NAME, "")
        editor.putString(Config.USERID, "")
        editor.putString(Config.TOKEN, "")
        editor.apply()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}