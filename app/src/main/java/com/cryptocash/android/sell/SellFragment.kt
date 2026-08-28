package com.cryptocash.android.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cryptocash.android.R
import com.cryptocash.android.databinding.FragmentSellBinding
import com.cryptocash.android.util.CurrencyFormatter
import com.cryptocash.android.util.HapticHelper

class SellFragment : Fragment() {

    private var _binding: FragmentSellBinding? = null
    private val binding get() = _binding!!

    private val rates = mapOf(
        "Bitcoin (BTC)" to 65_000.0,
        "Ethereum (ETH)" to 3_500.0,
        "Litecoin (LTC)" to 85.0,
        "Solana (SOL)" to 170.0,
        "USD Coin (USDC)" to 1.0
    )
    private val feePercent = 0.0175

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSellBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCoinDropdown()
        setupAmountInput()
        setupSellButton()
    }

    private fun setupCoinDropdown() {
        val coins = rates.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, coins)
        binding.dropdownCoin.setAdapter(adapter)
        binding.dropdownCoin.setText(coins[0], false)
        binding.dropdownCoin.setOnItemClickListener { _, _, _, _ -> updatePreview() }
    }

    private fun setupAmountInput() {
        binding.etCryptoAmount.doAfterTextChanged { updatePreview() }
    }

    private fun updatePreview() {
        val amount = binding.etCryptoAmount.text.toString().toDoubleOrNull() ?: return
        val coin = binding.dropdownCoin.text.toString()
        val rate = rates[coin] ?: return

        val grossUsd = amount * rate
        val fee = grossUsd * feePercent
        val netUsd = grossUsd - fee

        binding.tvFee.text = getString(R.string.sell_fee, CurrencyFormatter.formatFiat(requireContext(), fee))
        binding.tvUsdPreview.text = getString(
            R.string.sell_usd_preview,
            CurrencyFormatter.formatFiat(requireContext(), netUsd)
        )
    }

    private fun setupSellButton() {
        binding.btnReviewSell.setOnClickListener {
            val amount = binding.etCryptoAmount.text.toString().toDoubleOrNull()
            val coin = binding.dropdownCoin.text.toString()
            val destination = binding.tvSelectedDestination.text.toString()

            when {
                amount == null || amount <= 0 -> {
                    binding.tilCryptoAmount.error = getString(R.string.error_amount_required)
                    return@setOnClickListener
                }
                destination == getString(R.string.no_payment_method) -> {
                    binding.tvDestinationError.visibility = View.VISIBLE
                    return@setOnClickListener
                }
                else -> {
                    binding.tilCryptoAmount.error = null
                    binding.tvDestinationError.visibility = View.GONE
                    HapticHelper.performClick(requireContext())
                    val bundle = Bundle().apply {
                        putString("action", "sell")
                        putDouble("crypto_amount", amount)
                        putString("coin", coin)
                        putString("destination", destination)
                        putDouble("fee_percent", feePercent)
                    }
                    findNavController().navigate(R.id.action_sell_to_confirmation, bundle)
                }
            }
        }

        binding.btnSelectDestination.setOnClickListener {
            findNavController().navigate(R.id.action_sell_to_payment_methods)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
