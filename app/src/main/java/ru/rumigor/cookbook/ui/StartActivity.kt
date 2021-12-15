package ru.rumigor.cookbook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.rumigor.cookbook.AppPreferences
import ru.rumigor.cookbook.R
import ru.rumigor.cookbook.ui.ui.login.LoginActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        AppPreferences.setup(applicationContext)
        startActivity(LoginActivity.getStartIntent(this))
        finish()
    }
}