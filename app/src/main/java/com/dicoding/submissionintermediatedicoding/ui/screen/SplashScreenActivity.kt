package com.dicoding.submissionintermediatedicoding.ui.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.dicoding.submissionintermediatedicoding.MainActivity
import com.dicoding.submissionintermediatedicoding.R
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences
import com.dicoding.submissionintermediatedicoding.ui.auth.AuthActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val preferenceManager = UserLoginPreferences(this)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent = if (!preferenceManager.getStatusLogin()) {
                Intent(this@SplashScreenActivity, AuthActivity::class.java)
            } else {
                Intent(this@SplashScreenActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, delayStart.toLong())

    }
    companion object {
        private const val delayStart = 2000
    }
}