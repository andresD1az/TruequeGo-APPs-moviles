package com.example.truequego_apps_moviles.features.trades

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truequego_apps_moviles.domain.model.TradeProposal
import com.example.truequego_apps_moviles.domain.repository.AuthRepository
import com.example.truequego_apps_moviles.domain.repository.TradesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradesViewModel @Inject constructor(
    private val tradesRepository: TradesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _incomingProposals = MutableStateFlow<List<TradeProposal>>(emptyList())
    val incomingProposals: StateFlow<List<TradeProposal>> = _incomingProposals.asStateFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadIncomingProposals()
    }

    private fun loadIncomingProposals() {
        viewModelScope.launch {
            val userId = authRepository.currentUser?.uid ?: return@launch
            _isLoading.value = true
            tradesRepository.getIncomingProposals(userId).collectLatest { result ->
                result.onSuccess { list ->
                    _incomingProposals.value = list
                    _isLoading.value = false
                }
            }
        }
    }

    fun acceptProposal(proposalId: String) {
        viewModelScope.launch {
            tradesRepository.updateProposalStatus(proposalId, "ACCEPTED")
        }
    }

    fun rejectProposal(proposalId: String) {
        viewModelScope.launch {
            tradesRepository.updateProposalStatus(proposalId, "REJECTED")
        }
    }
}
