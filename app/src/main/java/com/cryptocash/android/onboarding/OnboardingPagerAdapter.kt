package com.cryptocash.android.onboarding

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(
    activity: FragmentActivity,
    private val pages: List<OnboardingPage>
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = pages.size

    override fun createFragment(position: Int) = OnboardingFragment.newInstance(pages[position])
}
