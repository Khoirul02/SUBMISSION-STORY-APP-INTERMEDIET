package com.submission.huda.storyapps.ui.registration

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.submission.huda.storyapps.databinding.ActivityRegistrasiBinding
import com.submission.huda.storyapps.ui.login.afterTextChanged

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var registrasiViewModel: RegistrasiViewModel
    private lateinit var binding: ActivityRegistrasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Registrasi"
        val name = binding.edRegisterName
        val username = binding.edRegisterEmail
        val password = binding.edRegisterPassword
        val daftar = binding.daftar
        val loading = binding.loading
        playAnimation()
        registrasiViewModel = ViewModelProvider(this)[RegistrasiViewModel::class.java]
        registrasiViewModel.regFormState.observe(this, Observer {
            val regState = it ?: return@Observer
            daftar.isEnabled = regState.isDataValid
            if (regState.usernameError != null) {
                username.error = getString(regState.usernameError)
            }
            if (regState.passwordError != null) {
                password.error = getString(regState.passwordError)
            }
        })
        registrasiViewModel.regResult.observe(this, Observer {
            val regResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (regResult.error != null) {
                daftar.visibility = View.VISIBLE
                showToast(regResult.error)
            }
            if (regResult.success != null) {
                daftar.visibility = View.VISIBLE
                name.setText("")
                username.setText("")
                password.setText("")
                showToastTrue(regResult.success)
            }
        })
        username.afterTextChanged {
            registrasiViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                registrasiViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }
        daftar.setOnClickListener {
            daftar.visibility = View.GONE
            loading.visibility = View.VISIBLE
            registrasiViewModel.reg(name.text.toString(),username.text.toString(), password.text.toString())
        }
    }
}
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val regis = ObjectAnimator.ofFloat(binding.relativeLayout, View.ALPHA, 1f).setDuration(2000)
        val together = AnimatorSet().apply {
            playTogether(regis)
        }

        AnimatorSet().apply {
            playSequentially(together)
            start()
        }
    }

    private fun showToast(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
    }
    private fun showToastTrue(model: RegistrasiView) {
        val message = model.message
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$message",
            Toast.LENGTH_LONG
        ).show()
    }
}