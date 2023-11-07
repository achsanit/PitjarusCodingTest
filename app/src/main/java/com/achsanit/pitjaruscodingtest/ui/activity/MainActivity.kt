package com.achsanit.pitjaruscodingtest.ui.activity

import android.Manifest.permission.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.achsanit.pitjaruscodingtest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    fun hasAllPermission() = EasyPermissions.hasPermissions(
        this,
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION,
        CAMERA,
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    fun requestAllPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cant work without location, storage, and camera",
            PERMISSION_REQUEST_CODE,
            ACCESS_FINE_LOCATION,
            CAMERA,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )
    }

    fun hasCameraPermission() = EasyPermissions.hasPermissions(
        this,
        CAMERA,
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    fun requestCameraPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cant work without location, storage, and camera",
            PERMISSION_REQUEST_CODE,
            CAMERA,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestAllPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Snackbar.make(
            binding.root,
            "Permission Granted!!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
}