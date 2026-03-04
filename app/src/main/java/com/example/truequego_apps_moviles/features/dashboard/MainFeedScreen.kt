package com.example.truequego_apps_moviles.features.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.truequego_apps_moviles.R
import com.example.truequego_apps_moviles.ui.theme.GrayText
import com.example.truequego_apps_moviles.ui.theme.LightBlueBg
import com.example.truequego_apps_moviles.ui.theme.OrangeAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFeedScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // States for interaction
    var searchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("Cercanos") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    
    val cities = listOf("Cercanos", "Armenia", "Calarcá", "Montenegro")
    val categories = listOf(
        CategoryData("Todos", null),
        CategoryData("Tecnología", Icons.Default.Devices),
        CategoryData("Libros", Icons.Default.MenuBook),
        CategoryData("Ropa", Icons.Default.Checkroom)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "trueque",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "GO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OrangeAccent
                    )
                }
                Row {
                    BadgedBox(
                        badge = { Badge { Text("3") } },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notificaciones",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(LightBlueBg)
                                .clickable { scope.launch { snackbarHostState.showSnackbar("Abriendo notificaciones...") } }
                                .padding(6.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Perfil",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LightBlueBg)
                            .clickable { scope.launch { snackbarHostState.showSnackbar("Abriendo tu perfil...") } }
                            .padding(6.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { scope.launch { snackbarHostState.showSnackbar("Buscando: $it") } },
                active = false,
                onActiveChange = {},
                placeholder = { Text("Buscar en truequeGO...", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = SearchBarDefaults.colors(containerColor = Color(0xFFF8FAFB))
            ) {}

            Spacer(modifier = Modifier.height(16.dp))

            // Filters Cities
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cities) { city ->
                    FilterChip(
                        selected = selectedCity == city,
                        label = city,
                        onClick = { selectedCity = city }
                    )
                }
            }

            // Categories
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    CategoryChip(
                        selected = selectedCategory == cat.label,
                        label = cat.label,
                        icon = cat.icon,
                        onClick = { selectedCategory = cat.label }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ProductCard(
                        title = "MacBook Pro 13\" 2019",
                        description = "Portátil en excelente estado, 8GB RAM, 256GB SSD. Batería en perfecto estado con solo 45 ciclos de...",
                        searchingFor = "iPhone 12 o superior +2",
                        ownerName = "Carlos Mendoza",
                        imageUrl = "https://images.unsplash.com/photo-1517336712462-d4c57a7996c3?auto=format&fit=crop&q=80&w=1024",
                        category = "Tecnología",
                        isVerified = true,
                        status = "Como nuevo",
                        likes = 24,
                        comments = 5,
                        onLikeClick = { scope.launch { snackbarHostState.showSnackbar("¡Te gusta este trueque!") } },
                        onCommentClick = { scope.launch { snackbarHostState.showSnackbar("Abriendo chat con Carlos...") } },
                        onTradeClick = { scope.launch { snackbarHostState.showSnackbar("Propuesta de trueque enviada") } }
                    )
                }
                item {
                    ProductCard(
                        title = "Libros de Diseño UI/UX",
                        description = "Colección de 5 libros sobre fundamentos de diseño y experiencia de usuario. Como nuevos.",
                        searchingFor = "Cámara reflex o similar",
                        ownerName = "Ana Garcia",
                        imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&q=80&w=1024",
                        category = "Libros",
                        isVerified = true,
                        status = "Leve uso",
                        likes = 12,
                        comments = 2,
                        onLikeClick = { scope.launch { snackbarHostState.showSnackbar("Guardado en favoritos") } },
                        onCommentClick = { scope.launch { snackbarHostState.showSnackbar("Iniciando chat con Ana...") } },
                        onTradeClick = { scope.launch { snackbarHostState.showSnackbar("Solicitud de intercambio enviada") } }
                    )
                }
            }
        }
    }
}

data class CategoryData(val label: String, val icon: ImageVector?)

@Composable
fun FilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFFF1F3F4),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CategoryChip(selected: Boolean, label: String, icon: ImageVector?, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (selected) OrangeAccent else Color(0xFFF1F3F4),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (selected) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                color = if (selected) Color.White else Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProductCard(
    title: String,
    description: String,
    searchingFor: String,
    ownerName: String,
    imageUrl: String,
    category: String,
    isVerified: Boolean,
    status: String,
    likes: Int,
    comments: Int,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onTradeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onTradeClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Highlights
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = category,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    if (isVerified) {
                        Surface(
                            color = Color(0xFF2ECC71),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, null, modifier = Modifier.size(12.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Verificada", color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Surface(color = Color(0xFFF1F3F4), shape = RoundedCornerShape(4.dp)) {
                        Text(status, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = GrayText)
                    }
                }
                
                Text(description, style = MaterialTheme.typography.bodyMedium, color = GrayText, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp))
                
                Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Sync, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Busca: ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(searchingFor, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFEEEEEE))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(36.dp), color = MaterialTheme.colorScheme.primary, shape = CircleShape) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(ownerName.first().toString(), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(ownerName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("Negociante", style = MaterialTheme.typography.labelSmall, color = OrangeAccent)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onLikeClick) {
                            Icon(Icons.Outlined.FavoriteBorder, null, modifier = Modifier.size(20.dp), tint = GrayText)
                        }
                        Text(likes.toString(), modifier = Modifier.padding(end = 8.dp), color = GrayText)
                        IconButton(onClick = onCommentClick) {
                            Icon(Icons.Outlined.ChatBubbleOutline, null, modifier = Modifier.size(20.dp), tint = GrayText)
                        }
                        Text(comments.toString(), color = GrayText)
                    }
                }
            }
        }
    }
}
