package com.cryptocash.android.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cryptocash.android.databinding.FragmentNotificationPreferencesBinding

class NotificationPreferencesFragment : Fragment() {

    private var _binding: FragmentNotificationPreferencesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPreferences()
        setupListeners()
    }

    private fun loadPreferences() {
        val prefs = requireContext().getSharedPreferences("notif_prefs", 0)
        binding.switchTransactions.isChecked = prefs.getBoolean("transactions", true)
        binding.switchPriceAlerts.isChecked = prefs.getBoolean("price_alerts", true)
        binding.switchSecurity.isChecked = prefs.getBoolean("security", true)
    }

    private fun setupListeners() {
        val prefs = requireContext().getSharedPreferences("notif_prefs", 0)
        binding.switchTransactions.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("transactions", checked).apply()
        }
        binding.switchPriceAlerts.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("price_alerts", checked).apply()
        }
        binding.switchSecurity.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("security", checked).apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
