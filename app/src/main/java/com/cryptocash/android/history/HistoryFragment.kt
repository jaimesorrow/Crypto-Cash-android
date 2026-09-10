package com.cryptocash.android.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptocash.android.R
import com.cryptocash.android.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TransactionAdapter

    // Mock data — replace with ViewModel + Room in production
    private val mockTransactions = listOf(
        TransactionItem("1", "buy", "Bought Bitcoin", "via Chase ••••4321", "+0.00152 BTC", "$98.75", true, "Confirmed", System.currentTimeMillis() - 3600_000L, R.drawable.ic_buy),
        TransactionItem("2", "send", "Sent to 1A2B…3C4D", "Bitcoin Network", "-0.005 BTC", "$325.00", false, "Confirmed", System.currentTimeMillis() - 86400_000L, R.drawable.ic_send),
        TransactionItem("3", "deposit", "Deposited Cash", "ACH Transfer", "+$500.00", "$500.00", true, "Pending", System.currentTimeMillis() - 172800_000L, R.drawable.ic_deposit),
        TransactionItem("4", "sell", "Sold Ethereum", "to Chase ••••4321", "-0.25 ETH", "+$875.00", true, "Confirmed", System.currentTimeMillis() - 259200_000L, R.drawable.ic_sell),
        TransactionItem("5", "receive", "Received Bitcoin", "from 5E6F…7G8H", "+0.01 BTC", "$650.00", true, "Confirmed", System.currentTimeMillis() - 345600_000L, R.drawable.ic_receive)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TransactionAdapter { item ->
            // TODO: navigate to transaction detail screen
        }
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter
        adapter.submitList(mockTransactions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
