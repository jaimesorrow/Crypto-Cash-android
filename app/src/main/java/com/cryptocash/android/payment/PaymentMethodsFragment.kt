package com.cryptocash.android.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptocash.android.R
import com.cryptocash.android.databinding.FragmentPaymentMethodsBinding
import com.cryptocash.android.util.HapticHelper

class PaymentMethodsFragment : Fragment() {

    private var _binding: FragmentPaymentMethodsBinding? = null
    private val binding get() = _binding!!

    // In a real app, these would come from a ViewModel / Room DB
    private val methods = mutableListOf(
        PaymentMethod("1", PaymentMethodType.BANK_ACCOUNT, "Chase Checking ••••4321", "Bank Account · ACH", isDefault = true, iconRes = R.drawable.ic_bank),
        PaymentMethod("2", PaymentMethodType.DEBIT_CARD, "Visa Debit ••••7890", "Visa Debit Card", iconRes = R.drawable.ic_card),
        PaymentMethod("3", PaymentMethodType.CREDIT_CARD, "Mastercard ••••1234", "Mastercard Credit", iconRes = R.drawable.ic_card)
    )

    private lateinit var adapter: PaymentMethodAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPaymentMethodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PaymentMethodAdapter(
            onSelect = { method ->
                HapticHelper.performClick(requireContext())
                // Mark as default
                methods.replaceAll { it.copy(isDefault = it.id == method.id) }
                adapter.submitList(methods.toList())
            },
            onDelete = { method ->
                methods.removeAll { it.id == method.id }
                adapter.submitList(methods.toList())
                updateEmptyState()
            }
        )

        binding.rvPaymentMethods.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPaymentMethods.adapter = adapter
        adapter.submitList(methods.toList())
        updateEmptyState()

        // Add new payment method options
        binding.btnAddBankAccount.setOnClickListener {
            HapticHelper.performClick(requireContext())
            showAddDialog(PaymentMethodType.BANK_ACCOUNT)
        }
        binding.btnAddDebitCard.setOnClickListener {
            HapticHelper.performClick(requireContext())
            showAddDialog(PaymentMethodType.DEBIT_CARD)
        }
        binding.btnAddCreditCard.setOnClickListener {
            HapticHelper.performClick(requireContext())
            showAddDialog(PaymentMethodType.CREDIT_CARD)
        }
    }

    private fun updateEmptyState() {
        binding.tvEmptyState.visibility = if (methods.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showAddDialog(type: PaymentMethodType) {
        AddPaymentMethodFragment.newInstance(type)
            .show(parentFragmentManager, "add_payment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
