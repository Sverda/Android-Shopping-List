package com.s24083.shoppinglist.ui.storesMap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.data.StoresRepository
import com.s24083.shoppinglist.data.model.Store
import com.s24083.shoppinglist.databinding.ActivityStoresMapBinding

class StoresMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoresMapBinding
    private lateinit var repo: StoresRepository
    private lateinit var stores: MutableLiveData<MutableList<Store>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoresMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        repo = StoresRepository.getInstance()
        stores = repo.getItems()
        stores.observe(this@StoresMapActivity, Observer {
            addMarkers(it!!)
        })
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
        mMap.isMyLocationEnabled = true
    }

    private fun addMarkers(localStores: MutableList<Store>) {
        var lastMarker = LatLng(52.2, 21.0)
        localStores.forEach { store ->
            val location = LatLng(
                store.location.split(",")[0].toDouble(),
                store.location.split(",")[1].toDouble())
            val markerOptions = MarkerOptions()
            markerOptions.position(location)
            markerOptions.title(store.name)
            markerOptions.snippet(store.description)
            mMap.addMarker(markerOptions)
            lastMarker = location
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastMarker))
    }
}