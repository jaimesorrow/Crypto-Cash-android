package com.cryptocash.android.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cryptocash.android.R
import com.cryptocash.android.databinding.FragmentConfirmationBinding
import com.cryptocash.android.util.HapticHelper

class ConfirmationFragment : Fragment() {

    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val action = arguments?.getString("action") ?: "send"
        val isBuySell = action == "buy" || action == "sell"

        binding.tvConfirmTitle.text = when (action) {
            "buy" -> getString(R.string.confirm_buy_title)
            "sell" -> getString(R.string.confirm_sell_title)
            else -> getString(R.string.confirm_send_title)
        }

        if (isBuySell) {
            val usdAmount = arguments?.getDouble("usd_amount") ?: arguments?.getDouble("crypto_amount") ?: 0.0
            val coin = arguments?.getString("coin") ?: ""
            val party = arguments?.getString("payment") ?: arguments?.getString("destination") ?: ""
            val feePercent = arguments?.getDouble("fee_percent") ?: 0.0
            val fee = usdAmount * feePercent

            binding.rowRecipient.visibility = View.GONE
            binding.rowPayment.visibility = View.VISIBLE
            binding.tvConfirmAmount.text = if (action == "buy") getString(R.string.usd_amount, usdAmount) else "$usdAmount $coin"
            binding.tvConfirmPayment.text = party
            binding.tvConfirmFee.text = getString(R.string.fee_display, fee)
        } else {
            binding.rowPayment.visibility = View.GONE
            binding.rowRecipient.visibility = View.VISIBLE
            binding.tvConfirmRecipient.text = arguments?.getString("recipient") ?: ""
            binding.tvConfirmAmount.text = "${arguments?.getString("amount")} ${arguments?.getString("currency")}"
            binding.tvConfirmFee.text = getString(R.string.fee_network)
            binding.tvConfirmRecipient.contentDescription =
                getString(R.string.confirm_recipient_cd, binding.tvConfirmRecipient.text)
        }

        binding.tvConfirmAmount.contentDescription =
            getString(R.string.confirm_amount_cd, binding.tvConfirmAmount.text)

        binding.btnConfirm.setOnClickListener {
            HapticHelper.performSuccess(requireContext())
            // TODO: submit transaction via ViewModel / repository
            findNavController().navigate(R.id.action_confirmation_to_home)
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
