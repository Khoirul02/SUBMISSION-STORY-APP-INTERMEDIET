package com.submission.huda.storyapps.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.submission.huda.storyapps.R

class MyEditTextPassword : AppCompatEditText, View.OnTouchListener {
    private lateinit var buttonImage: Drawable
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
        hint = resources.getString(R.string.password)

        // Menambahkan text aligmnet pada editText
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        buttonImage = ContextCompat.getDrawable(context, R.drawable.invisible) as Drawable
        setOnTouchListener(this)
        showButton()
        addTextChangedListener(onTextChanged = { p0, _, _, _ ->
            if(!isPasswordValid(p0.toString())) {
                setErorMsg()
            }
        })
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun showButton() {
        setButtonDrawables(endOfTheText = buttonImage)
    }

    private fun showButtonHide() {
        setButtonDrawables()
    }

    private fun setErorMsg(){
        val msg = resources.getString(R.string.invalid_password)
        error = msg
    }
    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText,
        )
    }
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (buttonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - buttonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            return if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        showButtonHide()
                        buttonImage = ContextCompat.getDrawable(context, R.drawable.view) as Drawable
                        showButton()
                        transformationMethod = HideReturnsTransformationMethod.getInstance()
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        showButtonHide()
                        buttonImage = ContextCompat.getDrawable(context, R.drawable.invisible) as Drawable
                        showButton()
                        transformationMethod = PasswordTransformationMethod.getInstance()
                        true
                    }
                    else -> false
                }
            } else false
        }
        return false
    }
}