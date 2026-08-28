package com.cryptocash.android.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cryptocash.android.R
import com.cryptocash.android.auth.SessionManager
import com.cryptocash.android.databinding.FragmentDepositBinding
import com.cryptocash.android.util.HapticHelper

class DepositFragment : Fragment() {

    private var _binding: FragmentDepositBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SessionManager(requireContext()).isGuestMode()) {
            binding.layoutGuestOverlay.visibility = View.VISIBLE
        }

        // Standard ACH (free, 1-3 business days)
        binding.cardAch.setOnClickListener {
            HapticHelper.performClick(requireContext())
            val bundle = Bundle().apply { putString("deposit_type", "ach") }
            findNavController().navigate(R.id.action_deposit_to_deposit_confirm, bundle)
        }

        // Instant deposit (1.5% fee)
        binding.cardInstant.setOnClickListener {
            HapticHelper.performClick(requireContext())
            val bundle = Bundle().apply { putString("deposit_type", "instant") }
            findNavController().navigate(R.id.action_deposit_to_deposit_confirm, bundle)
        }

        binding.btnManagePaymentMethods.setOnClickListener {
            findNavController().navigate(R.id.action_deposit_to_payment_methods)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
