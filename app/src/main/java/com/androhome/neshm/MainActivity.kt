package com.androhome.neshm

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.androhome.neshm.fragments.MapFragment
import com.androhome.neshm.fragments.ScheduleFragment
import com.androhome.neshm.fragments.homeFragment
import com.androhome.neshm.fragments.profileFragment
import com.androhome.neshm.model.NearbyProviderModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    private var permissionGranted: Boolean = false
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var lastKnownLocation: Location

    companion object {
        var nearbyProvidermodel: NearbyProviderModel.allUsers? = null
        var state: String? = null
        var latitude: Double = 0.0
        var longitude: Double = 0.0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        listeners()
//        swapFragment(homeFragment(), null, null, "bottombar_item_tutor")
        updateLocation()
    }

    private fun updateLocation() {
        if (!permissionGranted) {
            checkGpsAndNetworkState()
            getLocationPermission()
        }
        getDeviceLocation()
    }

    private fun listeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottombar_item_tutor -> {
                    swapFragment(homeFragment(), null, null, "bottombar_item_tutor")
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.bottombar_item_Map -> {
                    swapFragment(MapFragment(), null, null, "bottombar_item_Map")
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.bottombar_item_schedule -> {
                    swapFragment(ScheduleFragment(), null, null, "bottombar_item_schedule")
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.bottombar_item_profile -> {
                    swapFragment(profileFragment(), null, null, "bottombar_item_profile")
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    private fun initViews() {
        bottomNavigationView = findViewById(R.id.bottom_nav)
        val sharedpref: SharedPreferences =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        state = sharedpref.getString("state", "")
        latitude = (sharedpref.getString("lat", "") as String).toDouble()
        longitude = (sharedpref.getString("lang", "") as String).toDouble()
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    fun swapFragment(fragment: Fragment, args: Bundle?, previousFragment: Fragment?, fragmentTag: String) {
        if (args != null) {
            fragment.arguments = args
        }
        if (previousFragment != null) {
            supportFragmentManager.beginTransaction().remove(previousFragment).commitAllowingStateLoss()
            supportFragmentManager.popBackStack()
        }
        supportFragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, fragment)
            .addToBackStack(fragmentTag).commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            this.finish()
        }
//        Log.e("backStackEntryCount ",supportFragmentManager.backStackEntryCount.toString())
        swapFragment(homeFragment(), null, null, "bottombar_item_tutor")
        bottomNavigationView.selectedItemId = R.id.bottombar_item_tutor
    }

    fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionGranted = false
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true
                }
            }
        }
    }

    fun checkGpsAndNetworkState() {
        val lm: LocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled: Boolean = false
        var networkEnabled: Boolean = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!gpsEnabled && !networkEnabled) {
            permissionGranted = false
            AlertDialog.Builder(this)
                .setMessage("For better experience turn on device location, which uses Google's location service")
                .setPositiveButton("Settings") { dialog, which ->
                    this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }.setNegativeButton("Cancel", null).show()
        }
    }

    private fun getDeviceLocation() {
        try {
            if (permissionGranted) {
                val locationResult = locationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        lastKnownLocation = task.result!!
                        latitude = lastKnownLocation.latitude
                        longitude = lastKnownLocation.longitude
                        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
                        var addresses: List<Address> =
                            geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)
                        if (addresses.get(0).adminArea != null)
                            state = addresses.get(0).adminArea
                        swapFragment(homeFragment(), null, null, "bottombar_item_tutor")
                    } else {
                        swapFragment(homeFragment(), null, null, "bottombar_item_tutor")
                    }
                }
            } else {
                swapFragment(homeFragment(), null, null, "bottombar_item_tutor")
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }


}
