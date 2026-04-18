package com.example.truequego_apps_moviles.features.communities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.truequego_apps_moviles.ui.theme.*
import kotlinx.coroutines.launch

data class Community(val name: String, val description: String, val members: Int, val icon: String)

@Composable
fun CommunitiesScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showCreateDialog by remember { mutableStateOf(false) }
    var newCommunityName by remember { mutableStateOf("") }
    var newCommunityDesc by remember { mutableStateOf("") }
    val communities = remember {
        mutableStateListOf(
            Community("Tecnología Quindío", "Intercambia gadgets y dispositivos", 24, "💻"),
            Community("Libros Armenia", "Amantes de la lectura", 18, "📚"),
            Community("Ropa y Moda", "Intercambia prendas en buen estado", 31, "👗")
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = SurfaceContainerLowest,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = TertiaryAccent,
                contentColor = OnPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear comunidad")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Comunidades",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.weight(1f)
                )
            }

            if (communities.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Group, null, modifier = Modifier.size(64.dp), tint = SurfaceContainerHighest)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No hay comunidades aún.\nSé el primero en crear una.", textAlign = TextAlign.Center, color = OnSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(communities) { community ->
                        CommunityCard(
                            community = community,
                            onJoin = { scope.launch { snackbarHostState.showSnackbar("Te uniste a ${community.name}") } }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Crear comunidad", color = PrimaryNavy, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newCommunityName,
                        onValueChange = { newCommunityName = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = newCommunityDesc,
                        onValueChange = { newCommunityDesc = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCommunityName.isNotBlank()) {
                            communities.add(Community(newCommunityName.trim(), newCommunityDesc.trim(), 1, "🤝"))
                            newCommunityName = ""
                            newCommunityDesc = ""
                            showCreateDialog = false
                            scope.launch { snackbarHostState.showSnackbar("¡Comunidad creada!") }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) { Text("Crear") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun CommunityCard(community: Community, onJoin: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceContainerLow
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = PrimaryContainer) {
                Box(contentAlignment = Alignment.Center) {
                    Text(community.icon, style = MaterialTheme.typography.headlineMedium)
                }
            }
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(community.name, fontWeight = FontWeight.Bold, color = PrimaryNavy, style = MaterialTheme.typography.titleMedium)
                Text(community.description, color = OnSurfaceVariant, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 2.dp))
                Row(modifier = Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, null, modifier = Modifier.size(14.dp), tint = TertiaryAccent)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${community.members} miembros", style = MaterialTheme.typography.labelSmall, color = TertiaryAccent)
                }
            }
            OutlinedButton(
                onClick = onJoin,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryNavy)
            ) { Text("Unirse", fontWeight = FontWeight.Bold) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunitiesPreview() {
    TruequeGoAPPsmovilesTheme { CommunitiesScreen() }
}
