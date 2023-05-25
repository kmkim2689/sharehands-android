package com.sharehands.sharehands_frontend.view.signin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityPermissionsAgreeBinding

class PermissionsAgreeActivity: AppCompatActivity() {
    // 여기 안의 변수들은 전역적으로 사용할 것임.
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        private const val LOCATION_PERMISSION_REQUEST_CODE = 200
        private val REQUEST_CODES = arrayOf(CAMERA_PERMISSION_REQUEST_CODE, LOCATION_PERMISSION_REQUEST_CODE)
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
    }
    lateinit var binding: ActivityPermissionsAgreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions_agree)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permissions_agree)

        requestCameraPermission()
//        requestLocationPermission()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnNextActive.setOnClickListener {
            val intent = Intent(this, UserInfoWriteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission granted, do something
                } else {
                    // Location permission denied, show message or handle accordingly
                }
            }
//
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Location permission granted, do something
//                } else {
//                    // Location permission denied, show message or handle accordingly
//                }
//            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            // Permission already granted, do something
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permission already granted, do something
        }
    }
}