package com.achsanit.pitjaruscodingtest.util.extension

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

fun View.disable() {
    isEnabled = false
    isClickable = false
    alpha = 0.5F
}

fun View.enable() {
    isEnabled = true
    isClickable = true
    alpha = 1F
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun Fragment.getAddress(lat: Double, lng: Double): String? {
    var addressName: String? = null
    val geocoder = Geocoder(requireActivity().applicationContext, Locale.getDefault())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(lat, lng, 1) { list ->
            if (list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        }
    } else {
        try {
            val list = geocoder.getFromLocation(lat, lng, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
    return addressName
}

fun Fragment.getDistanceLocation(startLocation: LatLng, endLocation: LatLng): Float {
    val results = FloatArray(1)

    Location.distanceBetween(
        startLocation.latitude, startLocation.longitude,
        endLocation.latitude, endLocation.longitude,
        results
    )

    return results[0]
}