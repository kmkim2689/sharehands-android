package com.sharehands.sharehands_frontend.view.signin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityPermissionsAgreeBinding

class PermissionsAgreeActivity: AppCompatActivity() {
    // 전역적으로 사용
    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODE = 100
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.POST_NOTIFICATIONS)
    }
    lateinit var binding: ActivityPermissionsAgreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions_agree)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permissions_agree)

        checkPermissions()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnNextActive.setOnClickListener {
            val intent = Intent(this, UserInfoWriteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MULTIPLE_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty()) {
                    for((i, permission) in permissions.withIndex()) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                        }
                    }
                }
            }
        }
    }

    private fun checkPermissions() {
        var rejectedPermissionList = ArrayList<String>()

        for(permission in REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissionList.add(permission)
            }
        }

        if(rejectedPermissionList.isNotEmpty()){
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(this, rejectedPermissionList.toArray(array), MULTIPLE_PERMISSION_REQUEST_CODE)

        }
    }
}