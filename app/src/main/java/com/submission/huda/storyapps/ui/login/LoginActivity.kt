package com.submission.huda.storyapps.ui.login
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.submission.huda.storyapps.databinding.ActivityLoginBinding
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.ui.dashboard.DashboardActivity
import com.submission.huda.storyapps.ui.registration.RegistrasiActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val username = binding.edLoginEmail
        val password = binding.edLoginPassword
        val login = binding.login
        val loading = binding.loading
        val registration = binding.tvDaftar
        playAnimation()
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer
            // disable login button unless both username / password is valid
             login.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                username!!.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password!!.error = getString(loginState.passwordError)
            }
        })

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

        username!!.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password!!.text.toString()
            )
        }

        password.apply {
            this!!.afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password!!.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password!!.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                login.visibility = View.GONE
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password!!.text.toString())
            }
            registration.setOnClickListener {
                val intent = Intent(applicationContext, RegistrasiActivity::class.java)
                startActivity(intent)
            }
        }
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
    private fun showLoginTrue(model: LoggedInUserView) {
        val message = model.message
        val data = model.dataSession
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

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}