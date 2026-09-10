package com.cryptocash.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cryptocash.android.auth.AuthActivity
import com.cryptocash.android.auth.SessionManager
import com.cryptocash.android.onboarding.OnboardingActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1000L)
            val sessionManager = SessionManager(this@SplashActivity)
            val intent = if (sessionManager.isFirstRun()) {
                Intent(this@SplashActivity, OnboardingActivity::class.java)
            } else {
                Intent(this@SplashActivity, AuthActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
