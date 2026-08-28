package com.cryptocash.android.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cryptocash.android.MainActivity
import com.cryptocash.android.R
import com.cryptocash.android.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.btnContinueAsGuest.setOnClickListener {
            sessionManager.setGuestMode(true)
            navigateToMain()
        }

        when {
            BiometricHelper.isAvailable(this) && sessionManager.hasPinSet() -> showBiometric()
            sessionManager.hasPinSet() -> showPinVerify()
            else -> showPinSetup()
        }
    }

    private fun showBiometric() {
        BiometricHelper(
            activity = this,
            onSuccess = { navigateToMain() },
            onFallbackToPin = { showPinVerify() },
            onError = { showPinVerify() }
        ).authenticate()
    }

    private fun showPinVerify() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_container, PinFragment.newInstance(PinFragment.MODE_VERIFY))
            .commit()
    }

    private fun showPinSetup() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_container, PinFragment.newInstance(PinFragment.MODE_SETUP))
            .commit()
    }

    fun navigateToMain() {
        sessionManager.updateLastActive()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
