package com.example.truequego_apps_moviles.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.truequego_apps_moviles.ui.theme.GrayText
import com.example.truequego_apps_moviles.ui.theme.LightBlueBg
import com.example.truequego_apps_moviles.ui.theme.OrangeAccent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            item {
                HeaderSection(onBackClick = { scope.launch { snackbarHostState.showSnackbar("Regresando...") } })
            }
            item {
                StatsSection()
            }
            item {
                LevelsSection(onLevelClick = { scope.launch { snackbarHostState.showSnackbar("Detalles de nivel '$it'") } })
            }
            item {
                BadgesSection(onBadgeClick = { scope.launch { snackbarHostState.showSnackbar("Insignia '$it' desbloqueada") } })
            }
        }
    }
}

@Composable
fun HeaderSection(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        IconButton(onClick = onBackClick, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(4.dp, OrangeAccent)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("C", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            Text(
                "Christian Ríos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                color = OrangeAccent,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Negociante", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Bolt, null, modifier = Modifier.size(20.dp), tint = OrangeAccent)
                Text("340 / 500 puntos", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-30).dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(value = "12", label = "Activas", modifier = Modifier.weight(1f))
        StatCard(value = "8", label = "Finalizadas", modifier = Modifier.weight(1f))
        StatCard(value = "3", label = "Pendientes", modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier) {
    Surface(
        modifier = modifier.height(100.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 14.sp, color = GrayText)
        }
    }
}

@Composable
fun LevelsSection(onLevelClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("NIVELES", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
        Spacer(modifier = Modifier.height(16.dp))
        
        LevelItem("Novato", "0–99 pts", Icons.Default.Spa, isLocked = true, onClick = { onLevelClick("Novato") })
        LevelItem("Intercambiador", "100–249 pts", Icons.Default.Sync, isLocked = true, onClick = { onLevelClick("Intercambiador") })
        LevelItem("Negociante", "250–499 pts", Icons.Default.Handshake, isSelected = true, onClick = { onLevelClick("Negociante") })
        LevelItem("Maestro del Trueque", "500+ pts", Icons.Default.EmojiEvents, isLocked = true, onClick = { onLevelClick("Maestro del Trueque") })
    }
}

@Composable
fun LevelItem(
    title: String, 
    points: String, 
    icon: ImageVector, 
    isSelected: Boolean = false, 
    isLocked: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF8FAFB),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = if (isSelected) Color.White else if (isLocked) Color.LightGray else OrangeAccent)
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
                Text(points, fontSize = 12.sp, color = if (isSelected) Color.White.copy(alpha = 0.8f) else GrayText)
            }
            if (isSelected) {
                Icon(Icons.Default.ArrowLeft, null, tint = Color.White)
            }
        }
    }
}

@Composable
fun BadgesSection(onBadgeClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("INSIGNIAS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BadgeItem("Primer Intercambio", Icons.Default.Celebration, modifier = Modifier.weight(1f), onClick = { onBadgeClick("Primer Intercambio") })
            BadgeItem("Negociante Estrella", Icons.Default.Diamond, modifier = Modifier.weight(1f), onClick = { onBadgeClick("Negociante Estrella") })
            BadgeItem("Usuario Verificado", Icons.Default.CheckCircle, modifier = Modifier.weight(1f), onClick = { onBadgeClick("Usuario Verificado") })
        }
    }
}

@Composable
fun BadgeItem(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(140.dp),
        color = Color(0xFFF8FAFB),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, modifier = Modifier.size(40.dp), tint = OrangeAccent)
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, textAlign = TextAlign.Center, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
