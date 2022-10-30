package com.submission.huda.storyapps.helper

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.model.ListStoryItem
import com.submission.huda.storyapps.model.StoryResponse
import com.submission.huda.storyapps.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.contracts.contract

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private lateinit var sharedPreferences: SharedPreferences
val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())
var mWidgetItems = ArrayList<ListStoryItem>()
fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}
fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun reduceFileImageCameraX(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatedDate(date: String): String? {
    val current = LocalDate.parse(date.substring(0, 10))
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return current.format(formatter)
}

fun getDataBanner(context: Context): ArrayList<ListStoryItem> {
    sharedPreferences =  context.getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
    val token = "Bearer " + sharedPreferences.getString(Config.TOKEN,"")
    RetrofitClient.instance.getAllStory(token)
        .enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>?,
                response: Response<StoryResponse>?
            ) {
                val result = response!!.body()
                mWidgetItems =  result.listStory as ArrayList<ListStoryItem>
            }

            override fun onFailure(call: Call<StoryResponse>?, t: Throwable?) {
                TODO("Not yet implemented")
            }

        })
    return mWidgetItems
}