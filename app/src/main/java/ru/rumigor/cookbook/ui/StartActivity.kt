package ru.rumigor.cookbook.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.ui.login.LoginActivity

const val REQUEST_CODE = 777

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        AppPreferences.setup(applicationContext)
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET), REQUEST_CODE)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        startActivity(LoginActivity.getStartIntent(this))
        finish()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}