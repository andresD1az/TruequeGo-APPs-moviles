package com.example.truequego_apps_moviles.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.truequego_apps_moviles.navigation.NavRoutes
import com.example.truequego_apps_moviles.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AllInclusive, null, tint = PrimaryNavy, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("THE EXCHANGE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, color = PrimaryNavy)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(NavRoutes.Alerts.route) }) {
                        Icon(Icons.Outlined.Notifications, null, tint = PrimaryNavy)
                    }
                    IconButton(onClick = { navController.navigate(NavRoutes.Profile.route) }) {
                        Surface(modifier = Modifier.size(32.dp), shape = CircleShape, color = PrimaryNavy) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("E", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // BARRA LIMPIA: Sin manchas, solo iconos
            Surface(
                color = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier.fillMaxWidth().height(80.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabIcon("Inicio", Icons.Default.Home, Icons.Outlined.Home, currentDestination?.hierarchy?.any { it.route == NavRoutes.MainFeed.route } == true) {
                        navController.navigate(NavRoutes.MainFeed.route)
                    }
                    TabIcon("Trueques", Icons.Default.SwapHoriz, Icons.Outlined.SwapHoriz, currentDestination?.hierarchy?.any { it.route == NavRoutes.Trades.route } == true) {
                        navController.navigate(NavRoutes.Trades.route)
                    }
                    
                    // BOTÓN CENTRAL NARAJA
                    Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                        FloatingActionButton(
                            onClick = { navController.navigate(NavRoutes.Publish.route) },
                            containerColor = TertiaryAccent,
                            contentColor = Color.White,
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(26.dp))
                        }
                    }

                    TabIcon("Grupos", Icons.Default.Group, Icons.Outlined.Group, currentDestination?.hierarchy?.any { it.route == NavRoutes.Communities.route } == true) {
                        navController.navigate(NavRoutes.Communities.route)
                    }
                    TabIcon("Mapa", Icons.Default.Map, Icons.Outlined.Map, currentDestination?.hierarchy?.any { it.route == NavRoutes.Map.route } == true) {
                        navController.navigate(NavRoutes.Map.route)
                    }
                }
            }
        }
    ) { paddingValues ->
        // SCROLL HABILITADO: paddingValues aplicado al contenedor
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.MainFeed.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(NavRoutes.MainFeed.route) { MainFeedScreen() }
                composable(NavRoutes.Trades.route) { com.example.truequego_apps_moviles.features.trades.TradesScreen() }
                composable(NavRoutes.Map.route) { com.example.truequego_apps_moviles.features.map.MapScreen() }
                composable(NavRoutes.Alerts.route) { com.example.truequego_apps_moviles.features.alerts.AlertsScreen() }
                composable(NavRoutes.Communities.route) { com.example.truequego_apps_moviles.features.communities.CommunitiesScreen() }
                composable(NavRoutes.Profile.route) { com.example.truequego_apps_moviles.features.profile.ProfileScreen(onLogout = onLogout) }
                composable(NavRoutes.Publish.route) {
                    com.example.truequego_apps_moviles.features.publish.PublishScreen(onNavigateBack = { navController.popBackStack() })
                }
            }
        }
    }
}

@Composable
fun TabIcon(label: String, selIcon: androidx.compose.ui.graphics.vector.ImageVector, unselIcon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (isSelected) selIcon else unselIcon,
            contentDescription = label,
            tint = if (isSelected) PrimaryNavy else OnSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
        Text(text = label, fontSize = 10.sp, color = if (isSelected) PrimaryNavy else OnSurfaceVariant.copy(alpha = 0.5f), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}
