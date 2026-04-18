package com.example.truequego_apps_moviles.domain.repository

import com.example.truequego_apps_moviles.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getFeedProducts(): Flow<Result<List<Product>>>
    fun getProductsByUser(userId: String): Flow<Result<List<Product>>>
    fun addProduct(product: Product): Flow<Result<Unit>>
}
