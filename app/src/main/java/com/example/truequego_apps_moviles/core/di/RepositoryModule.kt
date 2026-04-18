package com.example.truequego_apps_moviles.core.di

import com.example.truequego_apps_moviles.data.repository.AuthRepositoryImpl
import com.example.truequego_apps_moviles.domain.repository.AuthRepository
import com.example.truequego_apps_moviles.data.repository.ProductRepositoryImpl
import com.example.truequego_apps_moviles.domain.repository.ProductRepository
import com.example.truequego_apps_moviles.data.repository.TradesRepositoryImpl
import com.example.truequego_apps_moviles.domain.repository.TradesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindTradesRepository(
        tradesRepositoryImpl: TradesRepositoryImpl
    ): TradesRepository
}
