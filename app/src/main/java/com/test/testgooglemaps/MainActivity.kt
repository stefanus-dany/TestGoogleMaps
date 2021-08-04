package com.test.testgooglemaps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.test.testgooglemaps.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    val TAG = "bro"
    var statusOfGPS: Boolean = false

    override fun onResume() {
        super.onResume()
//        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fl_wrapper, MapsFragment())
            commit()
        }
//
        binding.updateLocationBtn.setOnClickListener {
            PurchaseConfirmationDialogFragment().show(
                supportFragmentManager, PurchaseConfirmationDialogFragment.TAG
            )

            val mapIntent = Intent(Intent.ACTION_VIEW)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        }

        binding.locationBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i(TAG, "masuk cek versi android")
                if (applicationContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                ) {

                    Log.i(TAG, "diizinkan")
                    //get the location here
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        Log.i(TAG, "35")
                        if (it != null) {
                            Log.i(TAG, "37")
                            val lat = it.latitude
                            val long = it.longitude
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(lat, long, 1)
                            val address = addresses[0].getAddressLine(0)
//                            binding.locationText.text = address
                            binding.locationText.text = "lat : $lat, long : $long + $address"
                        } else {
                            PurchaseConfirmationDialogFragment().show(
                                supportFragmentManager, PurchaseConfirmationDialogFragment.TAG
                            )
                        }
                    }

                    fusedLocationProviderClient.lastLocation.addOnFailureListener {
                        Log.i(TAG, "45")
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.i(TAG, "blm permission")
                    requestPermissions(
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        0
                    )
                }
            } else Toast.makeText(
                this,
                "Your device doesn't support this feature!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.checkDistance.setOnClickListener {
//            val startPoint = Location("locationA")
//            startPoint.latitude = -6.8966497
//            startPoint.longitude = 107.6597795
//
//            val endPoint = Location("locationA")
//            endPoint.latitude = -6.89788500964
//            endPoint.longitude = 107.634174103
//
//            val distance = startPoint.distanceTo(endPoint)
//            Toast.makeText(this, "jarak $distance", Toast.LENGTH_SHORT).show()

//            val theta: Double = 107.6597795 - 107.634174103
//            var dist = (Math.sin(deg2rad(-6.8966497))
//                    * Math.sin(deg2rad(-6.89788500964))
//                    + (Math.cos(deg2rad(-6.8966497))
//                    * Math.cos(deg2rad(-6.89788500964))
//                    * Math.cos(deg2rad(theta))))
//            dist = Math.acos(dist)
//            dist = rad2deg(dist)
//            dist = dist * 60 * 1.1515*1609.34

            val Radius = 6371 // radius of earth in Km

            val lat1: Double = -6.8966497
            val lat2: Double = -6.8952097
            val lon1: Double = 107.6597795
            val lon2: Double = 107.659612
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + (Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2)))
            val c = 2 * Math.asin(Math.sqrt(a))
            val valueResult = Radius * c
            val km = valueResult / 1
            val newFormat = DecimalFormat("####")
            val kmInDec: Int = Integer.valueOf(newFormat.format(km))
            val meter = valueResult % 1000
            val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
            Log.i(
                "Radius Value", "" + valueResult + "   KM  " + kmInDec
                        + " Meter   " + meterInDec
            )

            val dist = Radius * c
            Toast.makeText(this, "jarak $dist", Toast.LENGTH_SHORT).show()
        }

    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}