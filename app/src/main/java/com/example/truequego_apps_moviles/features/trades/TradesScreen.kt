package com.example.truequego_apps_moviles.features.trades

import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.truequego_apps_moviles.ui.theme.GrayText
import com.example.truequego_apps_moviles.ui.theme.LightBlueBg
import com.example.truequego_apps_moviles.ui.theme.OrangeAccent
import kotlinx.coroutines.launch

@Composable
fun TradesScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Propuestas", "Chat", "Historial")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Mis Intercambios",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = OrangeAccent,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = OrangeAccent
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) OrangeAccent else GrayText
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        item {
                            Text("NUEVAS PROPUESTAS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
                        }
                        item {
                            ProposalCard(
                                ownerName = "Ana G.",
                                ownerRole = "Negociante",
                                time = "Hace 30 min",
                                message = "\"¡Hola! Me interesa mucho tu MacBook. Mi iPhone está en perfectas condiciones, batería al 95%.\"",
                                giveImg = "https://images.unsplash.com/photo-1517336712462-d4c57a7996c3?auto=format&fit=crop&q=80&w=200",
                                receiveImg = "https://images.unsplash.com/photo-1633113088452-6f29be607062?auto=format&fit=crop&q=80&w=200",
                                giveLabel = "MacBook Pro",
                                receiveLabel = "iPhone 13",
                                onAccept = {
                                    scope.launch { snackbarHostState.showSnackbar("Trueque aceptado. ¡Inicia el chat con Ana!") }
                                },
                                onReject = {
                                    scope.launch { snackbarHostState.showSnackbar("Trueque rechazado") }
                                }
                            )
                        }
                    }
                    1 -> {
                        item {
                            Text("CHATS ACTIVOS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
                        }
                        item {
                            ChatListItem(
                                name = "Carlos Mendoza",
                                lastMessage = "¿Aceptas el cambio por el iPhone?",
                                time = "10:30 AM",
                                unreadCount = 2,
                                onClick = { scope.launch { snackbarHostState.showSnackbar("Abriendo chat con Carlos...") } }
                            )
                        }
                        item {
                            ChatListItem(
                                name = "Ana Garcia",
                                lastMessage = "¡Perfecto! Nos vemos mañana.",
                                time = "Ayer",
                                unreadCount = 0,
                                onClick = { scope.launch { snackbarHostState.showSnackbar("Abriendo chat con Ana...") } }
                            )
                        }
                    }
                    2 -> {
                        item {
                            Text("HISTORIAL DE TRUEQUES", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = GrayText)
                        }
                        item {
                            HistoryItem(
                                title = "Cámara Reflex por Tablet",
                                date = "15 Feb 2024",
                                status = "Finalizado",
                                onClick = { scope.launch { snackbarHostState.showSnackbar("Viendo detalles del intercambio pasado") } }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatListItem(name: String, lastMessage: String, time: String, unreadCount: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFB)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = Color.LightGray) {
                Box(contentAlignment = Alignment.Center) {
                    Text(name.first().toString(), fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(lastMessage, fontSize = 12.sp, color = GrayText, maxLines = 1)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(time, fontSize = 10.sp, color = GrayText)
                if (unreadCount > 0) {
                    Surface(color = OrangeAccent, shape = CircleShape, modifier = Modifier.padding(top = 4.dp).size(18.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(unreadCount.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(title: String, date: String, status: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFB)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.History, null, modifier = Modifier.size(24.dp), tint = GrayText)
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(date, fontSize = 12.sp, color = GrayText)
            }
            Surface(color = Color(0xFF2ECC71).copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                Text(status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color(0xFF2ECC71), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProposalCard(
    ownerName: String,
    ownerRole: String,
    time: String,
    message: String,
    giveImg: String,
    receiveImg: String,
    giveLabel: String,
    receiveLabel: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Give
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("DAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GrayText)
                    AsyncImage(
                        model = giveImg,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(giveLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Icon(Icons.Default.SyncAlt, null, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)

                // Receive
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("RECIBES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GrayText)
                    AsyncImage(
                        model = receiveImg,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(receiveLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F3F4))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(40.dp), color = Color.LightGray, shape = CircleShape) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=100",
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(ownerName, fontWeight = FontWeight.Bold)
                    Text(ownerRole, fontSize = 12.sp, color = OrangeAccent)
                }
                Text(time, fontSize = 12.sp, color = GrayText)
            }

            Surface(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                color = Color(0xFFF8FAFB),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayText
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE74C3C)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE74C3C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Close, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Rechazar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
