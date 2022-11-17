package com.submission.huda.storyapps.ui.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.databinding.ActivityAddBinding
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.helper.reduceFileImageCameraX
import com.submission.huda.storyapps.helper.uriToFile
import com.submission.huda.storyapps.ui.ViewModelFactory
import com.submission.huda.storyapps.ui.dashboard.DashboardActivity
import java.io.File

class AddActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityAddBinding
    private lateinit var image: ImageView
    private var imageFile: File? = null
    private var imageKameraFile: File? = null
    private lateinit var addViewModel: AddViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Float? = null
    private var lon: Float? = null
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.tambah_data_story)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPreferences.getString(Config.TOKEN, "")
        val gallery = binding.galeri
        val camera = binding.kamera
        val description = binding.edAddDescription
        val add = binding.buttonAdd
        val progressBar = binding.loading
        image = binding.imgView
        addViewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance(this))[AddViewModel::class.java]
        addViewModel.addResult.observe(this, Observer {
            val addResult = it ?: return@Observer
            if (addResult.error !== null){
                add.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                showToast(addResult.error)
            }
            if (addResult.success !== null){
                add.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                showToastTrue(addResult.success)
            }
        })
        gallery.setOnClickListener {
            val intent = Intent()
            intent.action = ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }
        camera.setOnClickListener {
            startCameraX()
        }
        add.setOnClickListener {
            if (imageFile !== null) {
                add.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                uploadFormulir(token, lat!!, lon!! ,description.text.toString(), imageFile!!)
            } else if (imageKameraFile !== null) {
                add.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                uploadFormulir(token,lat!!, lon!!, description.text.toString(), imageKameraFile!!)
            } else {
                Toast.makeText(this, "Belum Pilih Gambar !!", Toast.LENGTH_LONG).show()
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude.toFloat()
                    lon = location.longitude.toFloat()
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showToast(error: String?) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
    }

    private fun showToastTrue(model: AddView?) {
        val message = model?.message
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(applicationContext, DashboardActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun uploadFormulir(token: String,lat: Float , lon: Float,description: String, imageFile: File) {
        addViewModel.addStory(token, lat, lon, description, imageFile)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            imageKameraFile = null
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            val resultFile = reduceFileImageCameraX(myFile)
            imageFile = resultFile
            image.setImageURI(selectedImg)
        }
    }

    private fun startCameraX() {
        val intent = Intent(applicationContext, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            imageFile = null
            val picture = it.data?.getSerializableExtra("picture") as File
            val resultFile = reduceFileImageCameraX(picture)
            imageKameraFile = resultFile
            val result = BitmapFactory.decodeFile(imageKameraFile!!.path)
            image.setImageBitmap(result)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}