package com.cryptocash.android.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cryptocash.android.R
import com.cryptocash.android.auth.SessionManager
import com.cryptocash.android.databinding.FragmentSendBinding
import com.cryptocash.android.util.CurrencyFormatter
import com.cryptocash.android.util.HapticHelper

class SendFragment : Fragment() {

    private var _binding: FragmentSendBinding? = null
    private val binding get() = _binding!!

    private val rates = mapOf("Bitcoin (BTC)" to 65_000.0, "Ethereum (ETH)" to 3_500.0, "Solana (SOL)" to 170.0)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SessionManager(requireContext()).isGuestMode()) {
            binding.layoutGuestOverlay.visibility = View.VISIBLE
        }

        setupCurrencyDropdown()
        setupAmountInput()
        setupSendButton()
    }

    private fun setupCurrencyDropdown() {
        val currencies = rates.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, currencies)
        binding.dropdownCurrency.setAdapter(adapter)
        binding.dropdownCurrency.setText(currencies[0], false)
        binding.dropdownCurrency.setOnItemClickListener { _, _, _, _ ->
            binding.etAmount.text?.clear()
            binding.tvFiatEquivalent.text = ""
        }
    }

    private fun setupAmountInput() {
        binding.etAmount.doAfterTextChanged { text ->
            val amount = text.toString().toDoubleOrNull() ?: 0.0
            val coin = binding.dropdownCurrency.text.toString()
            val rate = rates[coin] ?: 65_000.0
            val fiat = amount * rate
            binding.tvFiatEquivalent.text = getString(
                R.string.fiat_equivalent,
                CurrencyFormatter.formatFiat(requireContext(), fiat)
            )
        }
    }

    private fun setupSendButton() {
        binding.btnReviewSend.setOnClickListener {
            val recipient = binding.etRecipient.text.toString().trim()
            val amountStr = binding.etAmount.text.toString().trim()
            val currency = binding.dropdownCurrency.text.toString()

            when {
                recipient.isEmpty() -> {
                    binding.tilRecipient.error = getString(R.string.error_recipient_required)
                    return@setOnClickListener
                }
                amountStr.isEmpty() || amountStr.toDoubleOrNull() == null -> {
                    binding.tilAmount.error = getString(R.string.error_amount_required)
                    return@setOnClickListener
                }
                else -> {
                    binding.tilRecipient.error = null
                    binding.tilAmount.error = null
                    HapticHelper.performClick(requireContext())
                    val bundle = Bundle().apply {
                        putString("action", "send")
                        putString("recipient", recipient)
                        putString("amount", amountStr)
                        putString("currency", currency)
                        putString("fiat_preview", binding.tvFiatEquivalent.text.toString())
                    }
                    findNavController().navigate(R.id.action_send_to_confirmation, bundle)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
