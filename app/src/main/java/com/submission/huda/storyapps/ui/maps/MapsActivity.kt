package com.submission.huda.storyapps.ui.maps

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.databinding.ActivityMapsBinding
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.model.StoryResponse
import com.submission.huda.storyapps.ui.ViewModelFactory

@Suppress("NAME_SHADOWING")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mapsActivityViewModel: MapsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        getMyLocation()
        addManyMarker()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun addManyMarker() {
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPreferences.getString(Config.TOKEN, "")
        mapsActivityViewModel = ViewModelProvider(this,
            ViewModelFactory.getInstance(this)
        )[MapsActivityViewModel::class.java]
        mapsActivityViewModel.getAllStoryLatLong(token, 1).observe(this, Observer {
            val storyResult = it ?: return@Observer
            when (storyResult) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    if (storyResult.data.error == true) {
                        Toast.makeText(applicationContext, storyResult.data.message, Toast.LENGTH_LONG).show()
                    } else {
                        viewContentStory(storyResult.data)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(applicationContext, storyResult.exception, Toast.LENGTH_LONG).show()
                }
            }
        })
        getStoryLatLong(token, 1)
    }

    private fun getStoryLatLong(token: String, location : Int) {
        mapsActivityViewModel.getAllStoryLatLong(token, location)
    }

    private fun viewContentStory(data: StoryResponse) {
        val loop = data.listStory
        loop!!.forEach { tourism ->
            val latLng = LatLng(tourism!!.lat as Double, tourism.lon as Double)
            val description = tourism.description
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(tourism.name)
                    .snippet(description)
            )
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}
