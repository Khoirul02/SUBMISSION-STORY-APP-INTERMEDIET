package com.submission.huda.storyapps.ui.registration

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.databinding.ActivityRegistrasiBinding
import com.submission.huda.storyapps.helper.MyEditTextPassword
import com.submission.huda.storyapps.helper.MyEditTextUsername
import com.submission.huda.storyapps.ui.ViewModelFactory

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var registrasiViewModel: RegistrasiViewModel
    private lateinit var binding: ActivityRegistrasiBinding
    private lateinit var myEditTextUsername: MyEditTextUsername
    private lateinit var myEditTextPassword: MyEditTextPassword
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.reg)
        val name = binding.edRegisterName
        val daftar = binding.daftar
        val loading = binding.loading
        myEditTextUsername = findViewById(R.id.ed_register_email)
        myEditTextPassword = findViewById(R.id.ed_register_password)
        playAnimation()
        setMyButtonEnable()
        registrasiViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[RegistrasiViewModel::class.java]
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
                myEditTextUsername.setText("")
                myEditTextPassword.setText("")
                showToastTrue(regResult.success)
            }
        })
        daftar.setOnClickListener {
            daftar.visibility = View.GONE
            loading.visibility = View.VISIBLE
            registrasiViewModel.registration(name.text.toString(),myEditTextUsername.text.toString(), myEditTextPassword.text.toString())
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
        binding.edRegisterName.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            setMyButtonEnable()
        })
        myEditTextPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            setMyButtonEnable()
        })
        myEditTextUsername.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            setMyButtonEnable()
        })
    }

    private fun setMyButtonEnable() {
        val name = binding.edRegisterName.text
        val username = myEditTextUsername.text
        val password = myEditTextPassword.text
        val enabled = isUserNameValid(username.toString()) && isPasswordValid(password.toString()) && isUserFullName(name.toString())
        binding.daftar.isEnabled = enabled
    }

    // A placeholder fullname
    private fun isUserFullName(name: String): Boolean {
        return name.isNotEmpty()
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun showToast(error: String?) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
    }
    private fun showToastTrue(model: RegistrasiView?) {
        val message = model?.message
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$message",
            Toast.LENGTH_LONG
        ).show()
    }
}