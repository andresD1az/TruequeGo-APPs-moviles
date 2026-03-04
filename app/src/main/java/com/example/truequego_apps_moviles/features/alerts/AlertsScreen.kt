package com.example.truequego_apps_moviles.features.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.truequego_apps_moviles.ui.theme.GrayText
import com.example.truequego_apps_moviles.ui.theme.LightBlueBg
import com.example.truequego_apps_moviles.ui.theme.OrangeAccent
import kotlinx.coroutines.launch

@Composable
fun AlertsScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { scope.launch { snackbarHostState.showSnackbar("Regresando...") } }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Text(
                    "Notificaciones",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = { scope.launch { snackbarHostState.showSnackbar("Todas marcadas como leídas") } }) {
                    Text("Marcar leídas", color = OrangeAccent, fontWeight = FontWeight.Bold)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    SectionHeader("NUEVAS")
                }
                item {
                    NotificationItem(
                        title = "Carlos M. comentó en \"MacB...\"",
                        time = "Hace 5 min",
                        icon = Icons.Default.ChatBubble,
                        isNew = true,
                        onClick = { scope.launch { snackbarHostState.showSnackbar("Abriendo comentario de Carlos") } }
                    )
                }
                item {
                    NotificationItem(
                        title = "Ana G. propone un intercamb...",
                        time = "Hace 20 min",
                        icon = Icons.Default.Sync,
                        isNew = true,
                        onClick = { scope.launch { snackbarHostState.showSnackbar("Revisando propuesta de Ana") } }
                    )
                }
                item {
                    NotificationItem(
                        title = "Tu publicación \"MacBook Pro...\"",
                        time = "Hace 1 hora",
                        icon = Icons.Default.CheckCircle,
                        isNew = true,
                        onClick = { scope.launch { snackbarHostState.showSnackbar("Verificando publicación") } }
                    )
                }
                
                item {
                    SectionHeader("ANTERIORES")
                }
                item {
                    NotificationItem(
                        title = "Tu publicación 'MacBook Pro...",
                        time = "Hace 2 horas",
                        icon = Icons.Default.Favorite,
                        linkText = "Ver publicación",
                        onClick = { scope.launch { snackbarHostState.showSnackbar("Viendo detalles del producto") } }
                    )
                }
                item {
                    NotificationItem(
                        title = "¡Lograste la insignia 'Primer I...",
                        time = "Hace 3 horas",
                        icon = Icons.Default.Celebration,
                        linkText = "Ver insignia",
                        onClick = { scope.launch { snackbarHostState.showSnackbar("Insignia desbloqueada: ¡Felicidades!") } }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Surface(
        color = Color(0xFFF1F3F4),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = GrayText
        )
    }
}

@Composable
fun NotificationItem(
    title: String, 
    time: String, 
    icon: ImageVector, 
    isNew: Boolean = false, 
    linkText: String = "Ver",
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.White
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isNew) {
                    Box(modifier = Modifier.width(4.dp).height(50.dp).background(OrangeAccent))
                    Spacer(modifier = Modifier.width(12.dp))
                } else {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                
                Surface(
                    modifier = Modifier.size(48.dp),
                    color = if (isNew) LightBlueBg else Color(0xFFF1F3F4),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, null, modifier = Modifier.size(24.dp), tint = if (isNew) MaterialTheme.colorScheme.primary else Color.Gray)
                    }
                }
                
                Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(time, color = GrayText, fontSize = 12.sp)
                }
                
                TextButton(onClick = onClick) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(linkText, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}
