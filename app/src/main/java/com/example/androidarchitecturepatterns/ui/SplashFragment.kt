package com.example.androidarchitecturepatterns.ui

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.androidarchitecturepatterns.R
import com.example.androidarchitecturepatterns.utils.SampleMultiplePermissionListener
import com.example.androidarchitecturepatterns.databinding.FragmentSplashBinding
import com.example.androidarchitecturepatterns.utils.SharedPreferencesUtil
import com.example.androidarchitecturepatterns.utils.nextFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import java.io.IOException
import java.text.DateFormat
import java.util.*


class SplashFragment : Fragment() {

    private val TAG: String = SplashFragment::class.java.getSimpleName()
    private var mLastUpdateTime: String? = null

    // location updates interval - 10sec
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    private val REQUEST_CHECK_SETTINGS = 100

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates: Boolean? = null
    private var allPermissionsListener: MultiplePermissionsListener? = null
    var status = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_splash, container, false)
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createPermissionListeners()
        init()
        checkAppPermission()
        restoreValuesFromBundle(savedInstanceState)

//        lifecycleScope.launch(Dispatchers.Main){
//            delay(1000)
//
//        }
    }

    private fun createPermissionListeners() {
        val feedbackViewMultiplePermissionListener: MultiplePermissionsListener =
            SampleMultiplePermissionListener(this)

        allPermissionsListener = CompositeMultiplePermissionsListener(
            feedbackViewMultiplePermissionListener,
            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(
                requireView(),
                R.string.all_permissions_denied_feedback
            )
                .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                .build()
        )
    }


    private fun checkAppPermission() {
        Dexter.withContext(requireActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(allPermissionsListener)
            .check()
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(response: MultiplePermissionsReport?) {
//
//                    if (response!!.isAnyPermissionPermanentlyDenied) {
//                        Log.e("response", "" + response.deniedPermissionResponses)
//                        openSettings()
//                    } else {
//                        mRequestingLocationUpdates = true
//                        startLocationUpdates()
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    response: MutableList<PermissionRequest>?,
//                    token: PermissionToken?
//                ) {
//
//                }
//
//            }).check()

    }

    fun showPermissionGranted(permission: String?) {
        mRequestingLocationUpdates = true
        startLocationUpdates()
    }

    fun showPermissionDenied(permission: String?, isPermanentlyDenied: Boolean) {

    }

    private fun init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mSettingsClient = LocationServices.getSettingsClient(requireActivity())
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }
        mRequestingLocationUpdates = false
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            val lat = mCurrentLocation!!.latitude
            val lng = mCurrentLocation!!.longitude
            val latitude = lat.toString()
            val longitude = lng.toString()
            Log.e("latitude", "" + latitude)
            Log.e("Longitude", "" + longitude)
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(requireActivity(), Locale.getDefault())
            try {
                addresses = geocoder.getFromLocation(
                    lat,
                    lng,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                val address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                val knownName = addresses[0].featureName
                val countryCode = addresses[0].countryCode

//                SharedPreferencesUtil().setAddress(address,this)
//                SharedPreferencesUtil().setCity(city,this)
//                SharedPreferencesUtil().setState(state,this)
//                SharedPreferencesUtil().setZip(postalCode,this)


            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (status == true) {
                if(SharedPreferencesUtil().getUserId(requireActivity())!!.equals("")){
                    requireActivity().nextFragment(R.id.action_splashFragment_to_loginFragment)
                }
                else
                {
                    requireActivity().nextFragment(R.id.action_splashFragment_to_listFragment)
                }
            }
            else {

            }
        }
    }

    private fun restoreValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates")
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location")
            }
            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on")
            }
        }
        updateLocationUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates!!)
        outState.putParcelable("last_known_location", mCurrentLocation)
        outState.putString("last_updated_on", mLastUpdateTime)
    }


    private fun startLocationUpdates() {
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(requireActivity()) {
                Log.i(TAG, "All location settings are satisfied.")

                // Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
                updateLocationUI()
            }
            .addOnFailureListener(requireActivity()) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                updateLocationUI()
            }
    }

    fun stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(requireActivity()) {
                // Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
            }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun showPermissionRationale(token: PermissionToken) {
        AlertDialog.Builder(requireActivity()).setTitle(R.string.permission_rationale_title)
            .setMessage(R.string.permission_rationale_message)
            .setNegativeButton(android.R.string.cancel,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    token.cancelPermissionRequest()
                })
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    token.continuePermissionRequest()
                })
            .setOnDismissListener(DialogInterface.OnDismissListener { token.cancelPermissionRequest() })
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                AppCompatActivity.RESULT_OK -> Log.e(
                    TAG,
                    "User agreed to make required location settings changes."
                )
                AppCompatActivity.RESULT_CANCELED -> {
                    Log.e(TAG, "User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mRequestingLocationUpdates!! && checkPermissions()) {
            startLocationUpdates()
        }
        updateLocationUI()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    override fun onPause() {
        super.onPause()
        if (mRequestingLocationUpdates!!) {
            // pausing location updates
            stopLocationUpdates()
        }
    }

}