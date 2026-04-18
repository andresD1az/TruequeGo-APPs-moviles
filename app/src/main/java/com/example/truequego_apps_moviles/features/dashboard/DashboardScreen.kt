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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.truequego_apps_moviles.R
import com.example.truequego_apps_moviles.features.profile.ProfileViewModel
import com.example.truequego_apps_moviles.navigation.NavRoutes
import com.example.truequego_apps_moviles.ui.theme.*

sealed class BottomNavScreen(
    val route: String,
    val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home       : BottomNavScreen(NavRoutes.MainFeed.route,    R.string.dashboard_nav_home,        Icons.Filled.Home,       Icons.Outlined.Home)
    object Trades     : BottomNavScreen(NavRoutes.Trades.route,      R.string.dashboard_nav_trades,      Icons.Filled.SwapHoriz,  Icons.Outlined.SwapHoriz)
    object Communities: BottomNavScreen(NavRoutes.Communities.route, R.string.dashboard_nav_communities, Icons.Filled.Group,      Icons.Outlined.Group)
    object Map        : BottomNavScreen(NavRoutes.Map.route,         R.string.dashboard_nav_map,         Icons.Filled.Map,        Icons.Outlined.Map)
}

@Composable
fun DashboardScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    DashboardContent(navController = navController, onLogout = onLogout)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    navController: androidx.navigation.NavHostController,
    onLogout: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val userName by profileViewModel.userName.collectAsState()
    val userEmail by profileViewModel.userEmail.collectAsState()
    // Inicial para el avatar
    val initial = (userName.ifBlank { userEmail }).firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Scaffold(
        topBar = {
            Surface(
                color = SurfaceContainerLowest,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo
                    Icon(
                        Icons.Default.AllInclusive,
                        contentDescription = null,
                        tint = PrimaryNavy,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.dashboard_brand),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        letterSpacing = 2.sp
                    )

                    Spacer(Modifier.weight(1f))

                    // Campanita
                    IconButton(onClick = { navController.navigate(NavRoutes.Alerts.route) }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = PrimaryNavy)
                    }

                    Spacer(Modifier.width(4.dp))

                    // Avatar circular → navega al perfil
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { navController.navigate(NavRoutes.Profile.route) },
                        shape = CircleShape,
                        color = PrimaryNavy
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                initial,
                                color = OnPrimary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(SurfaceContainerLowest)
            ) {
                // Línea superior sutil
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SurfaceContainerLow)
                )

                val screens = listOf(
                    BottomNavScreen.Home,
                    BottomNavScreen.Trades,
                    null, // hueco para el FAB
                    BottomNavScreen.Communities,
                    BottomNavScreen.Map
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    screens.forEach { screen ->
                        if (screen == null) {
                            // Espacio vacío donde va el FAB
                            Spacer(modifier = Modifier.width(64.dp))
                        } else {
                            val isSelected = currentDestination?.hierarchy
                                ?.any { it.route == screen.route } == true

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = stringResource(screen.titleRes),
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isSelected) PrimaryNavy else OnSurfaceVariant
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = stringResource(screen.titleRes),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) PrimaryNavy else OnSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Spacer(Modifier.height(2.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .background(TertiaryAccent, CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavRoutes.Publish.route) },
                shape = CircleShape,
                containerColor = TertiaryAccent,
                contentColor = OnPrimary,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Publicar", modifier = Modifier.size(28.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.MainFeed.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(SurfaceContainerLowest)
        ) {
            composable(NavRoutes.MainFeed.route) {
                MainFeedScreen()
            }
            composable(NavRoutes.Trades.route) {
                com.example.truequego_apps_moviles.features.trades.TradesScreen()
            }
            composable(NavRoutes.Communities.route) {
                com.example.truequego_apps_moviles.features.communities.CommunitiesScreen()
            }
            composable(NavRoutes.Map.route) {
                com.example.truequego_apps_moviles.features.map.MapScreen()
            }
            composable(NavRoutes.Alerts.route) {
                com.example.truequego_apps_moviles.features.alerts.AlertsScreen()
            }
            composable(NavRoutes.Profile.route) {
                com.example.truequego_apps_moviles.features.profile.ProfileScreen(onLogout = onLogout)
            }
            composable(NavRoutes.Publish.route) {
                com.example.truequego_apps_moviles.features.publish.PublishScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    val navController = rememberNavController()
    TruequeGoAPPsmovilesTheme {
        DashboardContent(navController = navController, onLogout = {})
    }
}
