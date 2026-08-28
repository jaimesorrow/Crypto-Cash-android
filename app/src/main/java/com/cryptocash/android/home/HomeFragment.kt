package com.cryptocash.android.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cryptocash.android.R
import com.cryptocash.android.auth.SessionManager
import com.cryptocash.android.databinding.FragmentHomeBinding
import com.cryptocash.android.kyc.KycActivity
import com.cryptocash.android.util.CurrencyFormatter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SessionManager(requireContext()).isGuestMode()) {
            binding.bannerGuestMode.visibility = View.VISIBLE
        }

        observeViewModel()
        setupClickListeners()
        viewModel.loadBalance()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.skeletonBalance.visibility = if (loading) View.VISIBLE else View.GONE
            binding.layoutBalance.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.cryptoBalance.observe(viewLifecycleOwner) { balance ->
            binding.tvCryptoBalance.text = balance
            binding.tvCryptoBalance.contentDescription = getString(R.string.balance_crypto_cd, balance)
        }

        viewModel.fiatBalance.observe(viewLifecycleOwner) { fiat ->
            val formatted = CurrencyFormatter.formatFiat(requireContext(), fiat)
            binding.tvFiatBalance.text = formatted
            binding.tvFiatBalance.contentDescription = getString(R.string.balance_fiat_cd, formatted)
        }

        viewModel.error.observe(viewLifecycleOwner) { err ->
            binding.tvError.visibility = if (err != null) View.VISIBLE else View.GONE
            binding.btnRetry.visibility = if (err != null) View.VISIBLE else View.GONE
            binding.tvError.text = err
        }

        viewModel.kycRequired.observe(viewLifecycleOwner) { required ->
            binding.bannerKyc.visibility = if (required) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnBuy.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_buy)
        }
        binding.btnSell.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_sell)
        }
        binding.btnSend.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_send)
        }
        binding.btnDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_deposit)
        }
        binding.btnRetry.setOnClickListener { viewModel.loadBalance() }
        binding.bannerKyc.setOnClickListener {
            startActivity(Intent(requireContext(), KycActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
