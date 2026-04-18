package com.example.truequego_apps_moviles.features.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truequego_apps_moviles.domain.model.Product
import com.example.truequego_apps_moviles.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadFeedProducts()
    }

    private fun loadFeedProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            productRepository.getFeedProducts().collectLatest { result ->
                result.onSuccess { list ->
                    _products.value = list
                    _isLoading.value = false
                    _error.value = null
                }.onFailure {
                    _error.value = it.message
                    _isLoading.value = false
                }
            }
        }
    }
}
