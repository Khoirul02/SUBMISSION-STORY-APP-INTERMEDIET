package com.submission.huda.storyapps.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.adapter.ListStoryAdapter
import com.submission.huda.storyapps.adapter.LoadingStateAdapter
import com.submission.huda.storyapps.databinding.ActivityDashboardBinding
import com.submission.huda.storyapps.helper.Config
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
        rvStory.layoutManager = LinearLayoutManager(this)
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPreferences.getString(Config.TOKEN, "")
        dashboardViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[DashboardViewModel::class.java]
        rvStory.setHasFixedSize(true)
        adapter = ListStoryAdapter()
        rvStory.adapter = adapter
        rvStory.adapter= adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        dashboardViewModel.getAllStory(token).observe(this) {
            progressBar.visibility = View.GONE
            adapter.submitData(lifecycle,it)
        }
        add.setOnClickListener {
            val intent = Intent(applicationContext, AddActivity::class.java)
            startActivity(intent)
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