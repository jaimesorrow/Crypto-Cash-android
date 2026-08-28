package com.cryptocash.android.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingPage(
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
    @DrawableRes val imageRes: Int
)
