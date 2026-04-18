package com.example.truequego_apps_moviles.features.trades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.truequego_apps_moviles.R
import com.example.truequego_apps_moviles.ui.theme.*

@Composable
fun TradesScreen() {
    TradesContent()
}

@Composable
fun TradesContent() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabProposals = stringResource(R.string.trades_tab_proposals)
    val tabChat = stringResource(R.string.trades_tab_chat)
    val tabHistory = stringResource(R.string.trades_tab_history)
    val tabs = listOf(tabProposals, tabChat, tabHistory)

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
                    .background(SurfaceContainerLowest)
            ) {
                Text(
                    stringResource(R.string.trades_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        },
        containerColor = SurfaceContainerLowest
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceContainerLowest,
                contentColor = PrimaryNavy,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryNavy
                    )
                },
                divider = {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(SurfaceContainerLow))
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
                                color = if (selectedTab == index) PrimaryNavy else OnSurfaceVariant,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val (icon, message) = when (selectedTab) {
                        0 -> Icons.Default.SwapHoriz to "No tienes propuestas de intercambio aún"
                        1 -> Icons.Default.ChatBubbleOutline to "No tienes chats activos"
                        else -> Icons.Default.History to "No tienes intercambios en el historial"
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = SurfaceContainerHighest
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TradesPreview() {
    TruequeGoAPPsmovilesTheme { TradesContent() }
}
