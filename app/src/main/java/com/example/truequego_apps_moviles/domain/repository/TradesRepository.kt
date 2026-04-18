package com.example.truequego_apps_moviles.domain.repository

import com.example.truequego_apps_moviles.domain.model.TradeProposal
import kotlinx.coroutines.flow.Flow

interface TradesRepository {
    fun getIncomingProposals(userId: String): Flow<Result<List<TradeProposal>>>
    fun getOutgoingProposals(userId: String): Flow<Result<List<TradeProposal>>>
    fun createProposal(proposal: TradeProposal): Flow<Result<Unit>>
    fun updateProposalStatus(proposalId: String, newStatus: String): Flow<Result<Unit>>
}
