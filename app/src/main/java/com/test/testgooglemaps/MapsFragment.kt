package com.test.testgooglemaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.test.testgooglemaps.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding

    private lateinit var client: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
//    private var TAG = "bro"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.goBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocation()
            } else {
                //request permission
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    0
                )
            }
        }

        return binding.root
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 0 && (grantResults.isNotEmpty()) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
//            getCurrentLocation()
//        } else {
//            Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            client.lastLocation.addOnCompleteListener {
                val location = it.result
                //check condition
                if (location != null) {
                    binding.locationText.text =
                        "Latitude : ${location.latitude} and Longitude : ${location.longitude}"
                } else {
                    val locationRequest = LocationRequest.create().apply {
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        interval = 10000
                        fastestInterval = 1000
                        numUpdates = 1
                    }

                    //initialize location callback
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            val location1 = locationResult!!.lastLocation
                            binding.locationText.text =
                                "Latitude : ${location1.latitude} and Longitude : ${location1.longitude}"
                        }
                    }
                    //request location updates
                    val looper = Looper.myLooper() as Looper
                    client.requestLocationUpdates(locationRequest, locationCallback, looper)


                }
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}