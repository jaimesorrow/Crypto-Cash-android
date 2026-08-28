package com.cryptocash.android.buy

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
import com.cryptocash.android.databinding.FragmentBuyBinding
import com.cryptocash.android.util.CurrencyFormatter
import com.cryptocash.android.util.HapticHelper

class BuyFragment : Fragment() {

    private var _binding: FragmentBuyBinding? = null
    private val binding get() = _binding!!

    // Mock exchange rates (USD per 1 unit)
    private val rates = mapOf(
        "Bitcoin (BTC)" to 65_000.0,
        "Ethereum (ETH)" to 3_500.0,
        "Litecoin (LTC)" to 85.0,
        "Solana (SOL)" to 170.0,
        "USD Coin (USDC)" to 1.0
    )
    // Transaction fee: 1.75% spread monetization
    private val feePercent = 0.0175

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBuyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SessionManager(requireContext()).isGuestMode()) {
            binding.layoutGuestOverlay.visibility = View.VISIBLE
        }

        setupCoinDropdown()
        setupAmountInput()
        setupBuyButton()
    }

    private fun setupCoinDropdown() {
        val coins = rates.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, coins)
        binding.dropdownCoin.setAdapter(adapter)
        binding.dropdownCoin.setText(coins[0], false)
        binding.dropdownCoin.setOnItemClickListener { _, _, _, _ -> updateFeePreview() }
    }

    private fun setupAmountInput() {
        binding.etUsdAmount.doAfterTextChanged { updateFeePreview() }
    }

    private fun updateFeePreview() {
        val usd = binding.etUsdAmount.text.toString().toDoubleOrNull() ?: return
        val coin = binding.dropdownCoin.text.toString()
        val rate = rates[coin] ?: return

        val fee = usd * feePercent
        val netUsd = usd - fee
        val cryptoAmount = netUsd / rate

        binding.tvFee.text = getString(R.string.buy_fee, CurrencyFormatter.formatFiat(requireContext(), fee))
        binding.tvCryptoPreview.text = getString(
            R.string.buy_crypto_preview,
            CurrencyFormatter.formatCrypto(cryptoAmount, coin.substringAfterLast("(").trimEnd(')'))
        )
    }

    private fun setupBuyButton() {
        binding.btnReviewBuy.setOnClickListener {
            val usd = binding.etUsdAmount.text.toString().toDoubleOrNull()
            val coin = binding.dropdownCoin.text.toString()
            val paymentMethod = binding.tvSelectedPayment.text.toString()

            when {
                usd == null || usd <= 0 -> {
                    binding.tilUsdAmount.error = getString(R.string.error_amount_required)
                    return@setOnClickListener
                }
                paymentMethod == getString(R.string.no_payment_method) -> {
                    binding.tvPaymentError.visibility = View.VISIBLE
                    return@setOnClickListener
                }
                else -> {
                    binding.tilUsdAmount.error = null
                    binding.tvPaymentError.visibility = View.GONE
                    HapticHelper.performClick(requireContext())
                    val bundle = Bundle().apply {
                        putString("action", "buy")
                        putDouble("usd_amount", usd)
                        putString("coin", coin)
                        putString("payment", paymentMethod)
                        putDouble("fee_percent", feePercent)
                    }
                    findNavController().navigate(R.id.action_buy_to_confirmation, bundle)
                }
            }
        }

        binding.btnSelectPayment.setOnClickListener {
            findNavController().navigate(R.id.action_buy_to_payment_methods)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
