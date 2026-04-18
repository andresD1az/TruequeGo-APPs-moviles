package com.example.truequego_apps_moviles.data.repository

import com.example.truequego_apps_moviles.domain.model.Product
import com.example.truequego_apps_moviles.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    override fun getFeedProducts(): Flow<Result<List<Product>>> = callbackFlow {
        val subscription = firestore.collection("products")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val products = snapshot.toObjects(Product::class.java)
                    trySend(Result.success(products))
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getProductsByUser(userId: String): Flow<Result<List<Product>>> = callbackFlow {
        val subscription = firestore.collection("products")
            .whereEqualTo("ownerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val products = snapshot.toObjects(Product::class.java)
                    trySend(Result.success(products))
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun addProduct(product: Product): Flow<Result<Unit>> = callbackFlow {
        val document = firestore.collection("products").document()
        val newProduct = product.copy(id = document.id)
        
        document.set(newProduct)
            .addOnSuccessListener {
                trySend(Result.success(Unit))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose()
    }
}
