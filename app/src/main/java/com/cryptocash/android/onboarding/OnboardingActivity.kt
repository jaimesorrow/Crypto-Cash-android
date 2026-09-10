package com.cryptocash.android.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.cryptocash.android.R
import com.cryptocash.android.auth.AuthActivity
import com.cryptocash.android.auth.SessionManager
import com.cryptocash.android.databinding.ActivityOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private val pages by lazy {
        listOf(
            OnboardingPage(R.string.onboarding_title_1, R.string.onboarding_desc_1, R.drawable.ic_onboarding_wallet),
            OnboardingPage(R.string.onboarding_title_2, R.string.onboarding_desc_2, R.drawable.ic_onboarding_send),
            OnboardingPage(R.string.onboarding_title_3, R.string.onboarding_desc_3, R.drawable.ic_onboarding_secure)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingPagerAdapter(this, pages)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabIndicator, binding.viewPager) { _, _ -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val isLast = position == pages.size - 1
                binding.btnNext.text = if (isLast) getString(R.string.get_started) else getString(R.string.next)
            }
        })

        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < pages.size - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                finishOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener { finishOnboarding() }
    }

    private fun finishOnboarding() {
        SessionManager(this).setFirstRunComplete()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
