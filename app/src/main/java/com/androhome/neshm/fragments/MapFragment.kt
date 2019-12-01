package com.androhome.neshm.fragments


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.androhome.neshm.MainActivity
import com.androhome.neshm.R
import com.androhome.neshm.interfaces.LoginApiInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var geoDataClient: GeoDataClient
    private lateinit var placeDetectionClient: PlaceDetectionClient
    private var permissionGranted: Boolean = false
    private lateinit var map: GoogleMap
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var lastKnownLocation: Location
    var longitude: Double = 0.0
    var latitude: Double = 0.0
    var state: String = ""
    var tempState: String = ""
    lateinit var markerOptions: MarkerOptions

    val loginApiInterface by lazy {
        LoginApiInterface.create()
    }
    var disposable: Disposable? = null

    /**
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        initViews(view)
        return view
    }

    private fun providerNetworkCall() {
        markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizingmarker(R.drawable.remotemarker)))

        if (MainActivity.nearbyProvidermodel == null || tempState != state) {
            val sharedPref = requireContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            ) ?: return
            with(sharedPref.edit()) {
                putString("tempState", state)
                apply()
            }
            MainActivity.state = this.state
            map.clear()
            disposable = loginApiInterface.nearbyProviders(state).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                    if (result != null) {
                        if (result.userInfo!!.size > 0 && result.userInfo!!.get(0).result != "Something Wrong" && result.userInfo!!.get(
                                0
                            ).result != "Search failed"
                        ) {
                            MainActivity.nearbyProvidermodel = result
                            for (data in result.userInfo!!) {
                                val lat: Double = data.latitude!!.toDouble()
                                val long: Double = data.longitude!!.toDouble()
                                markerOptions.position(LatLng(lat, long)).title(data.name)
                                map.addMarker(markerOptions)
                            }
                        }
                    }
                }, { error ->
                    Log.e("Map fragment", error.toString())
                    Toast.makeText(requireContext(), "Unable to fetch data", Toast.LENGTH_SHORT).show()
                })
        } else {
            map.clear()
            val result = MainActivity.nearbyProvidermodel
            for (data in result?.userInfo!!) {
                val lat: Double = data.latitude!!.toDouble()
                val long: Double = data.longitude!!.toDouble()
                markerOptions.position(LatLng(lat, long)).title(data.name)
                map.addMarker(markerOptions)
            }
        }
    }


    /**
     *
     */
    private fun initViews(view: View) {
        supportMapFragment = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        geoDataClient = Places.getGeoDataClient(requireContext(), null)
        placeDetectionClient = Places.getPlaceDetectionClient(requireContext(), null)
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val sharedpref: SharedPreferences =
            requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        latitude = sharedpref.getString("lat", "").toDouble()
        longitude = sharedpref.getString("lang", "").toDouble()
        this.state = sharedpref.getString("state", "")
        tempState = sharedpref.getString("tempState", "")

    }

    /**
     *
     */
    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        updateLocationUI()
        getDeviceLocation()
//        providerNetworkCall()
    }

    private fun getDeviceLocation() {
        markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizingmarker(R.drawable.currentmarker)))
        try {
            if (permissionGranted) {
                map.isMyLocationEnabled = true
                val locationResult = locationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful && task.result != null) {
                        lastKnownLocation = task.result!!
                        latitude = lastKnownLocation.latitude
                        longitude = lastKnownLocation.longitude
                        MainActivity.latitude = latitude
                        MainActivity.longitude = longitude
                        map.clear()
                        val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
                        var addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
                        if (addresses.get(0).adminArea != null) {
                            this.state = addresses.get(0).adminArea
                        }
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation.latitude
                                    , lastKnownLocation.longitude
                                ), 13f
                            )
                        )
                        //                        markerOptions.position(
                        //                            LatLng(
                        //                                lastKnownLocation.latitude
                        //                                , lastKnownLocation.longitude
                        //                            )
                        //                        )
                        //                        map.addMarker(markerOptions)
                        providerNetworkCall()
                    } else {
                        if (latitude != 0.0 && longitude != 0.0) {
                            MainActivity.latitude = latitude
                            MainActivity.longitude = longitude
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        latitude, longitude
                                    ), 13f
                                )
                            )
                            providerNetworkCall()
                            //                            markerOptions.position(
                            //                                LatLng(
                            //                                    latitude
                            //                                    , longitude
                            //                                )
                            //                            )
                            //                            map.addMarker(markerOptions)
                        }
                    }
                }
            } else {
                providerNetworkCall()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        } else {
            try {
                if (permissionGranted) {
                    map.isMyLocationEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    map.isMyLocationEnabled = false
                    map.uiSettings.isMyLocationButtonEnabled = false
                    checkGpsAndNetworkState()
                    getLocationPermission()
                }
            } catch (e: SecurityException) {
                Log.e("Exception: %s", e.message);
            }
        }
    }

    /**
     *
     */
    fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                context!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
            requireContext().applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
            AlertDialog.Builder(requireContext())
                .setMessage("For better experience turn on device location, which uses Google's location service")
                .setPositiveButton("Settings") { dialog, which ->
                    requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }.setNegativeButton("Cancel", null).show()
        }
    }

    override fun onResume() {
        val currentFragment: Fragment? = fragmentManager?.findFragmentByTag("Map")
        val fragmentTransaction: androidx.fragment.app.FragmentTransaction = fragmentManager!!.beginTransaction()
        if (currentFragment != null) {
            fragmentTransaction.detach(currentFragment)
        }
        if (currentFragment != null) {
            fragmentTransaction.attach(currentFragment)
        }
        fragmentTransaction.commit()
        super.onResume()
    }

    fun resizingmarker(id: Int): Bitmap {
        val height = 100
        val width = 100
        val bitmapdraw: BitmapDrawable = getResources().getDrawable(id) as BitmapDrawable
        val b: Bitmap = bitmapdraw.getBitmap()
        val smallMarker: Bitmap = Bitmap.createScaledBitmap(b, width, height, false)
        return smallMarker
    }
}
