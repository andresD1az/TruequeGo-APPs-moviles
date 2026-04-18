package com.example.truequego_apps_moviles.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.truequego_apps_moviles.domain.model.Product
import com.example.truequego_apps_moviles.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MainFeedScreen(viewModel: MainFeedViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading
    MainFeedContent(products = products, isLoading = isLoading)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFeedContent(products: List<Product>, isLoading: Boolean) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("Cercanos") }
    val cities = listOf("Cercanos", "Armenia", "Calarcá", "Montenegro")

    Box(modifier = Modifier.fillMaxSize().background(SurfaceContainerLowest)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Buscador
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { },
                        active = false,
                        onActiveChange = {},
                        placeholder = { Text("Buscar en The Exchange...", color = OnSurfaceVariant) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = PrimaryNavy) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = SearchBarDefaults.colors(
                            containerColor = SurfaceContainerLow,
                            dividerColor = Color.Transparent
                        )
                    ) {}
                }
            }

            // Filtros ciudad
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cities) { city ->
                        FilterChip(
                            selected = selectedCity == city,
                            label = city,
                            onClick = { selectedCity = city }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Contenido
            when {
                isLoading -> item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryNavy)
                    }
                }
                products.isEmpty() -> item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(300.dp).padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.Inventory2, null, modifier = Modifier.size(64.dp), tint = SurfaceContainerHighest)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay publicaciones aún.\nPresiona + para publicar la primera.",
                                textAlign = TextAlign.Center,
                                color = OnSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                else -> items(products) { product ->
                    ProductCard(
                        product = product,
                        modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp),
                        onTradeClick = { scope.launch { snackbarHostState.showSnackbar("Propuesta iniciada") } }
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, onTradeClick: () -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth().clickable { onTradeClick() },
        shape = RoundedCornerShape(16.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                AsyncImage(
                    model = if (product.imageUrl.isNotEmpty()) product.imageUrl
                    else "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&q=80&w=800",
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(12.dp),
                    color = PrimaryNavy.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        "GENERAL",
                        color = OnPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 1.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        product.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryNavy,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    if (product.condition.isNotBlank()) {
                        Surface(color = SurfaceContainerLow, shape = RoundedCornerShape(4.dp)) {
                            Text(
                                product.condition,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
                if (product.description.isNotBlank()) {
                    Text(
                        product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                if (product.location.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = TertiaryAccent)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(product.location, color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onTradeClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.SwapHoriz, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Proponer intercambio", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (selected) PrimaryNavy else SurfaceContainerLow,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            color = if (selected) OnPrimary else OnSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainFeedPreview() {
    TruequeGoAPPsmovilesTheme {
        MainFeedContent(
            products = listOf(
                Product(title = "MacBook Pro", description = "M1 Pro 14 pulgadas", condition = "Nuevo", location = "Armenia"),
                Product(title = "Cámara Sony", description = "Alpha A7III con lente", condition = "Usado", location = "Calarcá")
            ),
            isLoading = false
        )
    }
}
