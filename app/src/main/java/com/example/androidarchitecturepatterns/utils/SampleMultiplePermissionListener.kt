package com.example.androidarchitecturepatterns.utils

import com.example.androidarchitecturepatterns.ui.SplashFragment
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class SampleMultiplePermissionListener : MultiplePermissionsListener {

    private var activity: SplashFragment? = null

    constructor(activity: SplashFragment?){
        this.activity = activity
    }


    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        for (response in report!!.getGrantedPermissionResponses()) {
            activity!!.showPermissionGranted(response.permissionName)
        }

        for (response in report!!.getDeniedPermissionResponses()) {
            activity!!.showPermissionDenied(response.permissionName, response.isPermanentlyDenied)
        }
    }

    override fun onPermissionRationaleShouldBeShown(
            permissions: MutableList<PermissionRequest>?,
            token: PermissionToken?
    ) {
        activity!!.showPermissionRationale(token!!)
    }
}