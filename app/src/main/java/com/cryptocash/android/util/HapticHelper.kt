package com.cryptocash.android.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object HapticHelper {

    fun performClick(context: Context) = vibrate(context, 30)

    fun performSuccess(context: Context) = vibrate(context, longArrayOf(0, 50, 50, 100))

    fun performError(context: Context) = vibrate(context, longArrayOf(0, 100, 50, 100, 50, 100))

    private fun vibrate(context: Context, duration: Long) {
        val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
        getVibrator(context).vibrate(effect)
    }

    private fun vibrate(context: Context, pattern: LongArray) {
        val effect = VibrationEffect.createWaveform(pattern, -1)
        getVibrator(context).vibrate(effect)
    }

    private fun getVibrator(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}
