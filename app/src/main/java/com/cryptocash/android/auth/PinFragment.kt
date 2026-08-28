package com.cryptocash.android.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cryptocash.android.R
import com.cryptocash.android.databinding.FragmentPinBinding
import com.cryptocash.android.util.HapticHelper
import java.security.MessageDigest

class PinFragment : Fragment() {

    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!

    private val currentPin = StringBuilder()
    private var firstPin = ""
    private lateinit var sessionManager: SessionManager

    companion object {
        const val MODE_SETUP = "setup"
        const val MODE_VERIFY = "verify"
        private const val ARG_MODE = "mode"

        fun newInstance(mode: String) = PinFragment().apply {
            arguments = Bundle().apply { putString(ARG_MODE, mode) }
        }
    }

    private val mode get() = arguments?.getString(ARG_MODE) ?: MODE_VERIFY

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        binding.tvPinTitle.text = if (mode == MODE_SETUP)
            getString(R.string.pin_create_title) else getString(R.string.pin_enter_title)
        setupNumpad()
    }

    private fun setupNumpad() {
        // btn0..btn9 mapped by digit value
        mapOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9"
        ).forEach { (btn, digit) ->
            btn.setOnClickListener {
                HapticHelper.performClick(requireContext())
                appendDigit(digit)
            }
        }
        binding.btnDelete.setOnClickListener {
            HapticHelper.performClick(requireContext())
            if (currentPin.isNotEmpty()) {
                currentPin.deleteCharAt(currentPin.length - 1)
                updateDots()
            }
        }
    }

    private fun appendDigit(digit: String) {
        if (currentPin.length >= 6) return
        currentPin.append(digit)
        updateDots()
        if (currentPin.length == 6) handlePinComplete()
    }

    private fun updateDots() {
        listOf(binding.dot1, binding.dot2, binding.dot3,
               binding.dot4, binding.dot5, binding.dot6)
            .forEachIndexed { i, dot -> dot.isSelected = i < currentPin.length }
    }

    private fun handlePinComplete() {
        when (mode) {
            MODE_SETUP -> {
                if (firstPin.isEmpty()) {
                    firstPin = currentPin.toString()
                    currentPin.clear()
                    updateDots()
                    binding.tvPinTitle.text = getString(R.string.pin_confirm_title)
                } else {
                    if (firstPin == currentPin.toString()) {
                        sessionManager.setPinHash(hashPin(currentPin.toString()))
                        (activity as? AuthActivity)?.navigateToMain()
                    } else {
                        binding.tvPinError.visibility = View.VISIBLE
                        binding.tvPinError.text = getString(R.string.pin_mismatch)
                        HapticHelper.performError(requireContext())
                        currentPin.clear(); firstPin = ""
                        updateDots()
                        binding.tvPinTitle.text = getString(R.string.pin_create_title)
                    }
                }
            }
            MODE_VERIFY -> {
                if (hashPin(currentPin.toString()) == sessionManager.getPinHash()) {
                    (activity as? AuthActivity)?.navigateToMain()
                } else {
                    binding.tvPinError.visibility = View.VISIBLE
                    binding.tvPinError.text = getString(R.string.pin_incorrect)
                    HapticHelper.performError(requireContext())
                    currentPin.clear()
                    updateDots()
                }
            }
        }
    }

    private fun hashPin(pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(pin.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
