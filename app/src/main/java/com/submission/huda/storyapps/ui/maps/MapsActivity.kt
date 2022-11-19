package com.submission.huda.storyapps.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.databinding.ActivityMapsBinding
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.helper.vectorToBitmap
import com.submission.huda.storyapps.model.ListStoryItem
import com.submission.huda.storyapps.ui.ViewModelFactory

@Suppress("NAME_SHADOWING")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
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
        mMap.setOnMarkerClickListener(this)
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
        mapsActivityViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[MapsActivityViewModel::class.java]
        mapsActivityViewModel.getAllStoryLatLong(token, 1).observe(this, Observer {
            val storyResult = it ?: return@Observer
            when (storyResult) {
                is Result.Success -> {
                    if (storyResult.data.error == true) {
                        Toast.makeText(
                            applicationContext,
                            storyResult.data.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val data = storyResult.data.listStory
                        if (data.isNotEmpty()) {
                            viewContentStory(data)
                        }
                    }
                }
                is Result.Error -> {
                    Toast.makeText(applicationContext, storyResult.exception, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
        getStoryLatLong(token)
    }

    private fun getStoryLatLong(token: String) {
        mapsActivityViewModel.getAllStoryLatLong(token, 1)
    }

    private fun viewContentStory(data: List<ListStoryItem>) {
        mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this,data))
        data.forEach { data ->
            val latLng = LatLng(data.lat as Double, data.lon as Double)
            val description = data.description
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(data.id)
                    .snippet(description)
                    .icon(
                        vectorToBitmap(
                            applicationContext.resources,
                            R.drawable.ic_android,
                            Color.parseColor("#3DDC84")
                        )
                    )
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

    class CustomInfoWindowForGoogleMap(
        context: Context,
        dataListItem: List<ListStoryItem>
    ) : GoogleMap.InfoWindowAdapter {
        private var mContext = context
        private var list = dataListItem

        @SuppressLint("InflateParams")
        private val customView: View =
            (context as Activity).layoutInflater.inflate(R.layout.costume_info_windows, null)

        private fun setPopUp(marker: Marker, view: View) {
            val name = view.findViewById<TextView>(R.id.tv_user_name)
            val photo = view.findViewById<ImageView>(R.id.iv_photo)
            val progressBar = view.findViewById<ProgressBar>(R.id.progress)
            val result = list.find { s -> s.id == marker.title }
            name.text = result!!.name
            Glide.with(mContext)
                .load(result.photoUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        photo.visibility = VISIBLE
                        progressBar.visibility = GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        photo.visibility = VISIBLE
                        progressBar.visibility = GONE
                        return false
                    }
                })
                .into(photo)
        }

        override fun getInfoContents(marker: Marker): View? {
            return null
        }

        override fun getInfoWindow(marker: Marker): View {
            setPopUp(marker, customView)
            return customView
        }
    }

    override fun onMarkerClick(maker: Marker): Boolean {
        maker.showInfoWindow()
        return true
    }
}
