package com.submission.huda.storyapps.helper

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.submission.huda.storyapps.R

class MyEditTextUsername : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Menambahkan hint pada editText
        hint = resources.getString(R.string.email)

        // Menambahkan text aligmnet pada editText
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(onTextChanged = { p0, _, _, _ ->
            if(!isUserNameValid(p0.toString())){
                setErorMsg()
            }
        })
    }
    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }
    private fun setErorMsg(){
        val msg = resources.getString(R.string.invalid_username)
        error = msg
    }
}