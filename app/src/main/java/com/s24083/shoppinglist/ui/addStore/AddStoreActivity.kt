package com.s24083.shoppinglist.ui.addStore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.s24083.shoppinglist.databinding.ActivityAddStoreBinding

class AddStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoreBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val currentStoreId: Int? get() {
        val bundle: Bundle? = intent.extras
        return bundle?.getInt("id")
    }

    private val viewModel by viewModels<AddStoreViewModel>{
        AddStoreViewModelFactory(this, currentStoreId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoreBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.doneButton.setOnClickListener {
            addOrUpdateStore()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext!!)
        getLocationUpdates()
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // start receiving location update when activity visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun addOrUpdateStore() {
        val resultIntent = Intent()
        if (validate()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
            finish()
        }

        val name = binding.addStoreName.text.toString()
        val description = binding.addStoreDescription.text.toString()
        val radius = binding.addStoreRadius.text.toString().toInt()
        val location = binding.addStoreLocation.text.toString()
        resultIntent.putExtra("id", currentStoreId)
        resultIntent.putExtra("name", name)
        resultIntent.putExtra("description", description)
        resultIntent.putExtra("radius", radius)
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun validate() : Boolean {
        return binding.addStoreName.text.isNullOrEmpty()
                || binding.addStoreDescription.text.isNullOrEmpty()
                || binding.addStoreRadius.text.isNullOrEmpty()
                || binding.addStoreLocation.text.isNullOrEmpty();
    }

    private fun getLocationUpdates()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext!!)
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.smallestDisplacement = 10f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location = locationResult.lastLocation
                    // use your location object
                    // get latitude , longitude and other info from this
                    viewModel?.let{
                        it.location.value = "${location.latitude}, ${location.longitude}"
                    }
                }
            }
        }
    }

    //start location updates
    private fun startLocationUpdates() {
        val perms =
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        val requestCode = 1
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(perms, requestCode)
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}