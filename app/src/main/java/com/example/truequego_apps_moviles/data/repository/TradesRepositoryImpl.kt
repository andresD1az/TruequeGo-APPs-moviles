package com.example.truequego_apps_moviles.data.repository

import com.example.truequego_apps_moviles.domain.model.TradeProposal
import com.example.truequego_apps_moviles.domain.repository.TradesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TradesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TradesRepository {

    override fun getIncomingProposals(userId: String): Flow<Result<List<TradeProposal>>> = callbackFlow {
        val subscription = firestore.collection("proposals")
            .whereEqualTo("receiverId", userId)
            .whereEqualTo("status", "PENDING")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val proposals = snapshot.toObjects(TradeProposal::class.java)
                    trySend(Result.success(proposals))
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getOutgoingProposals(userId: String): Flow<Result<List<TradeProposal>>> = callbackFlow {
        val subscription = firestore.collection("proposals")
            .whereEqualTo("senderId", userId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val proposals = snapshot.toObjects(TradeProposal::class.java)
                    trySend(Result.success(proposals))
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun createProposal(proposal: TradeProposal): Flow<Result<Unit>> = callbackFlow {
        val doc = firestore.collection("proposals").document()
        val newProposal = proposal.copy(id = doc.id)
        
        doc.set(newProposal)
            .addOnSuccessListener { trySend(Result.success(Unit)) }
            .addOnFailureListener { trySend(Result.failure(it)) }
        awaitClose()
    }

    override fun updateProposalStatus(proposalId: String, newStatus: String): Flow<Result<Unit>> = callbackFlow {
        firestore.collection("proposals").document(proposalId)
            .update("status", newStatus)
            .addOnSuccessListener { trySend(Result.success(Unit)) }
            .addOnFailureListener { trySend(Result.failure(it)) }
        awaitClose()
    }
}
