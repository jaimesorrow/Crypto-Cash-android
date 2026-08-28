package com.cryptocash.android.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptocash.android.data.repository.WalletRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = WalletRepository()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _cryptoBalance = MutableLiveData("--")
    val cryptoBalance: LiveData<String> = _cryptoBalance

    private val _fiatBalance = MutableLiveData(0.0)
    val fiatBalance: LiveData<Double> = _fiatBalance

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _kycRequired = MutableLiveData(false)
    val kycRequired: LiveData<Boolean> = _kycRequired

    fun loadBalance() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val balance = repository.getBalance()
                _cryptoBalance.value = balance.cryptoFormatted
                _fiatBalance.value = balance.fiatUsd
                _kycRequired.value = balance.kycRequired
            } catch (e: Exception) {
                _error.value = "Unable to load balance. Please check your connection."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
