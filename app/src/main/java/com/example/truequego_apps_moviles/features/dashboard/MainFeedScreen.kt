package com.example.truequego_apps_moviles.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.truequego_apps_moviles.domain.model.Product
import com.example.truequego_apps_moviles.ui.theme.*

@Composable
fun MainFeedScreen(viewModel: MainFeedViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading
    
    // IMPORTANTE: Aquí NO debe haber otro Scaffold, para que el scroll sea del Dashboard
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceContainerLow)
    ) {
        // Buscador superior
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar en The Exchange...", color = OnSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = PrimaryNavy) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = SurfaceContainerLowest,
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = PrimaryNavy
                )
            )
        }

        // Lista con Scroll real
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 100.dp), // Espacio extra abajo para el botón +
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val filters = listOf("Cercanos", "Armenia", "Calarcá", "Montenegro")
                    items(filters) { city ->
                        FilterChip(label = city, selected = city == "Cercanos")
                    }
                }
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryNavy)
                    }
                }
            } else {
                items(products) { product ->
                    ProductCard(product)
                }
            }
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean) {
    Surface(
        color = if (selected) PrimaryNavy else SurfaceContainerLowest,
        shape = RoundedCornerShape(8.dp),
        onClick = {}
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) OnPrimary else OnSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProductCard(product: Product) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceContainerLowest
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl.ifBlank { "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&q=80&w=1024" },
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(product.title, style = MaterialTheme.typography.titleLarge, color = PrimaryNavy, fontWeight = FontWeight.Bold)
                    Surface(color = SurfaceContainerLow, shape = RoundedCornerShape(6.dp)) {
                        Text(product.condition, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall)
                    }
                }
                Text(product.description, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 8.dp))
                
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Sync, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Propone un intercambio")
                }
            }
        }
    }
}
