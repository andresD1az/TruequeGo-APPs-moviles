package com.example.truequego_apps_moviles.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.truequego_apps_moviles.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    
    ProfileContent(
        userName = userName, 
        userEmail = userEmail, 
        onLogout = {
            viewModel.logout()
            onLogout()
        }
    )
}

@Composable
fun ProfileContent(
    userName: String = "",
    userEmail: String = "",
    onLogout: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = SurfaceContainerLowest
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            item {
                ProfileHeaderSection(
                    userName = userName,
                    userEmail = userEmail,
                    onBackClick = { scope.launch { snackbarHostState.showSnackbar("Regresando...") } },
                    onLogout = onLogout
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                LevelsSection(onLevelClick = { level ->
                    scope.launch { snackbarHostState.showSnackbar("Nivel: $level") }
                })
            }
            item {
                BadgesSection(onBadgeClick = { badge ->
                    scope.launch { snackbarHostState.showSnackbar("Insignia: $badge") }
                })
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun ProfileHeaderSection(
    userName: String,
    userEmail: String,
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    val displayName = userName.ifBlank { if (userEmail.isNotBlank()) userEmail else "Usuario" }
    val initial = displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Box(modifier = Modifier.fillMaxWidth().height(260.dp).background(SurfaceContainerLow)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = PrimaryNavy)
            }
            IconButton(onClick = onLogout) {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = PrimaryNavy)
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = SurfaceContainerHighest) {
                Box(contentAlignment = Alignment.Center) {
                    Text(initial, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                }
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
            if (userEmail.isNotBlank() && userEmail != displayName) {
                Text(text = userEmail, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun LevelsSection(onLevelClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("NIVELES", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        LevelItem("Novato", "0–99 pts", Icons.Default.Spa, isSelected = true, onClick = onLevelClick)
    }
}

@Composable
fun LevelItem(title: String, points: String, icon: ImageVector, isSelected: Boolean = false, onClick: (String) -> Unit) {
    Surface(onClick = { onClick(title) }, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), color = if (isSelected) PrimaryContainer else SurfaceContainerLowest, shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = if (isSelected) PrimaryNavy else SurfaceContainerLow, shape = CircleShape, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, null, modifier = Modifier.size(20.dp), tint = OnPrimary) }
            }
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                Text(points, style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun BadgesSection(onBadgeClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("INSIGNIAS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BadgeItem("Nuevo", Icons.Default.Celebration, modifier = Modifier.weight(1f), onClick = onBadgeClick)
            BadgeItem("Verificado", Icons.Default.CheckCircle, modifier = Modifier.weight(1f), onClick = onBadgeClick)
        }
    }
}

@Composable
fun BadgeItem(title: String, icon: ImageVector, modifier: Modifier, onClick: (String) -> Unit) {
    Surface(onClick = { onClick(title) }, modifier = modifier.height(120.dp), color = SurfaceContainerLowest, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, null, modifier = Modifier.size(32.dp), tint = TertiaryAccent)
            Text(title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryNavy, textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview() {
    TruequeGoAPPsmovilesTheme {
        ProfileContent(userName = "Usuario Real", userEmail = "usuario@correo.com")
    }
}
