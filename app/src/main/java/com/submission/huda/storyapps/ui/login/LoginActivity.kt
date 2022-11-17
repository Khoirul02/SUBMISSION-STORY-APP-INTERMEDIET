package com.submission.huda.storyapps.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.databinding.ActivityLoginBinding
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.helper.MyEditTextPassword
import com.submission.huda.storyapps.helper.MyEditTextUsername
import com.submission.huda.storyapps.ui.ViewModelFactory
import com.submission.huda.storyapps.ui.dashboard.DashboardActivity
import com.submission.huda.storyapps.ui.registration.RegistrasiActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myEditTextPassword: MyEditTextPassword
    private lateinit var myEditTextUsername: MyEditTextUsername
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val login = binding.login
        val loading = binding.loading
        val registration = binding.tvDaftar
        myEditTextUsername = findViewById(R.id.ed_login_email)
        myEditTextPassword = findViewById(R.id.ed_login_password)
        playAnimation()
        setMyButtonEnable()
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[LoginViewModel::class.java]
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (loginResult.error != null) {
                login.visibility = View.VISIBLE
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                login.visibility = View.VISIBLE
                showLoginTrue(loginResult.success)
            }
        })

        login.setOnClickListener {
            login.visibility = View.GONE
            loading.visibility = View.VISIBLE
            loginViewModel.login(
                myEditTextUsername.text.toString(),
                myEditTextPassword.text.toString()
            )
        }
        registration.setOnClickListener {
            val intent = Intent(applicationContext, RegistrasiActivity::class.java)
            startActivity(intent)
        }
        myEditTextPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            setMyButtonEnable()
        })
        myEditTextUsername.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            setMyButtonEnable()
        })
    }

    private fun setMyButtonEnable() {
        val username = myEditTextUsername.text
        val password = myEditTextPassword.text
        val enable = isUserNameValid(username.toString()) && isPasswordValid(password.toString())
        binding.login.isEnabled = enable
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.relativeLayout, View.ALPHA, 1f).setDuration(2000)
        val together = AnimatorSet().apply {
            playTogether(login)
        }

        AnimatorSet().apply {
            playSequentially(together)
            start()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun showLoginTrue(model: LoggedInUserView?) {
        val message = model?.message
        val data = model?.dataSession
        val editor: SharedPreferences.Editor? = sharedPreferences.edit()
        editor!!.putString(Config.NAME, data!!.name)
        editor.putString(Config.USERID, data.userId)
        editor.putString(Config.TOKEN, data.token)
        editor.apply()
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$message",
            Toast.LENGTH_LONG
        ).show()
        openDashboard()
    }

    private fun showLoginFailed(errorString: String?) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun openDashboard() {
        val intent = Intent(applicationContext, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}