package com.cryptocash.android.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cryptocash.android.R
import com.cryptocash.android.databinding.FragmentDepositConfirmBinding
import com.cryptocash.android.util.CurrencyFormatter
import com.cryptocash.android.util.HapticHelper

class DepositConfirmFragment : Fragment() {

    private var _binding: FragmentDepositConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDepositConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val depositType = arguments?.getString("deposit_type") ?: "ach"
        val isInstant = depositType == "instant"

        binding.tvDepositTypeTitle.text = if (isInstant)
            getString(R.string.deposit_instant_title)
        else
            getString(R.string.deposit_ach_title)

        binding.tvDepositTypeDesc.text = if (isInstant)
            getString(R.string.deposit_instant_desc)
        else
            getString(R.string.deposit_ach_desc)

        // Update fee row
        binding.tvDepositFeeLabel.text = if (isInstant)
            getString(R.string.deposit_fee_instant)
        else
            getString(R.string.deposit_fee_free)

        binding.etDepositAmount.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val amount = s.toString().toDoubleOrNull() ?: 0.0
                val fee = if (isInstant) amount * 0.015 else 0.0
                val net = amount - fee
                binding.tvFeeAmount.text = CurrencyFormatter.formatFiat(requireContext(), fee)
                binding.tvNetAmount.text = CurrencyFormatter.formatFiat(requireContext(), net)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnConfirmDeposit.setOnClickListener {
            val amount = binding.etDepositAmount.text.toString().toDoubleOrNull()
            if (amount == null || amount <= 0) {
                binding.tilDepositAmount.error = getString(R.string.error_amount_required)
                return@setOnClickListener
            }
            HapticHelper.performSuccess(requireContext())
            // TODO: submit deposit via repository
            findNavController().navigate(R.id.action_deposit_confirm_to_home)
        }

        binding.btnCancel.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
