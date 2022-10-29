package com.submission.huda.storyapps.model

import com.google.gson.annotations.SerializedName

data class RegistrasiResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
