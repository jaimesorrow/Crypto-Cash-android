package com.cryptocash.android.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptocash.android.data.local.AppDatabase
import com.cryptocash.android.data.local.TransactionDao
import com.cryptocash.android.data.local.TransactionEntity
import com.cryptocash.android.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TransactionAdapter { /* No detail screen yet — row tap is a no-op for now */ }
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter

        val dao = AppDatabase.getInstance(requireContext()).transactionDao()

        viewLifecycleOwner.lifecycleScope.launch {
            seedIfEmpty(dao)
            dao.getAllTransactions().collect { entities ->
                adapter.submitList(entities.map { it.toTransactionItem(requireContext()) })
            }
        }
    }

    /**
     * First-run sample data so History isn't blank before any buy/sell/send/deposit has
     * happened. Room is the source of truth from here on — new transactions inserted by
     * the confirmation flows simply show up via the Flow collected above.
     */
    private suspend fun seedIfEmpty(dao: TransactionDao) {
        if (dao.getCount() > 0) return
        val now = System.currentTimeMillis()
        listOf(
            TransactionEntity("seed-1", "buy", 0.00152, "BTC", 98.75, "Chase ••••4321", now - 3_600_000L, "Confirmed"),
            TransactionEntity("seed-2", "send", 0.005, "BTC", 325.00, "1A2B…3C4D", now - 86_400_000L, "Confirmed"),
            TransactionEntity("seed-3", "deposit", 0.0, "USD", 500.00, "ACH Transfer", now - 172_800_000L, "Pending"),
            TransactionEntity("seed-4", "sell", 0.25, "ETH", 875.00, "Chase ••••4321", now - 259_200_000L, "Confirmed"),
            TransactionEntity("seed-5", "receive", 0.01, "BTC", 650.00, "5E6F…7G8H", now - 345_600_000L, "Confirmed")
        ).forEach { dao.insert(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
