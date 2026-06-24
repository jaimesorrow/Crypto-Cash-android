package com.cryptocash.android.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cryptocash.android.R
import com.cryptocash.android.databinding.FragmentAddPaymentMethodBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddPaymentMethodFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddPaymentMethodBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_TYPE = "type"

        fun newInstance(type: PaymentMethodType) = AddPaymentMethodFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_TYPE, type) }
        }
    }

    private val type: PaymentMethodType
        get() = (arguments?.getSerializable(ARG_TYPE) as? PaymentMethodType) ?: PaymentMethodType.BANK_ACCOUNT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (type) {
            PaymentMethodType.BANK_ACCOUNT -> {
                binding.tvSheetTitle.text = getString(R.string.add_bank_account)
                binding.layoutCardFields.visibility = View.GONE
                binding.layoutBankFields.visibility = View.VISIBLE
            }
            PaymentMethodType.DEBIT_CARD, PaymentMethodType.CREDIT_CARD -> {
                binding.tvSheetTitle.text = if (type == PaymentMethodType.DEBIT_CARD)
                    getString(R.string.add_debit_card) else getString(R.string.add_credit_card)
                binding.layoutBankFields.visibility = View.GONE
                binding.layoutCardFields.visibility = View.VISIBLE
            }
        }

        binding.btnSavePaymentMethod.setOnClickListener {
            if (validateForm()) {
                // TODO: save to ViewModel / repository
                Toast.makeText(requireContext(), getString(R.string.payment_method_added), Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        binding.btnCancelPaymentMethod.setOnClickListener { dismiss() }
    }

    private fun validateForm(): Boolean {
        return when (type) {
            PaymentMethodType.BANK_ACCOUNT -> {
                val routing = binding.etRoutingNumber.text.toString().trim()
                val account = binding.etAccountNumber.text.toString().trim()
                when {
                    routing.length != 9 -> {
                        binding.tilRoutingNumber.error = getString(R.string.error_routing_invalid)
                        false
                    }
                    account.length < 4 -> {
                        binding.tilAccountNumber.error = getString(R.string.error_account_invalid)
                        false
                    }
                    else -> { binding.tilRoutingNumber.error = null; binding.tilAccountNumber.error = null; true }
                }
            }
            else -> {
                val card = binding.etCardNumber.text.toString().replace(" ", "")
                val expiry = binding.etExpiry.text.toString().trim()
                val cvv = binding.etCvv.text.toString().trim()
                when {
                    card.length < 15 -> {
                        binding.tilCardNumber.error = getString(R.string.error_card_invalid)
                        false
                    }
                    expiry.length != 5 -> {
                        binding.tilExpiry.error = getString(R.string.error_expiry_invalid)
                        false
                    }
                    cvv.length < 3 -> {
                        binding.tilCvv.error = getString(R.string.error_cvv_invalid)
                        false
                    }
                    else -> { binding.tilCardNumber.error = null; binding.tilExpiry.error = null; binding.tilCvv.error = null; true }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
