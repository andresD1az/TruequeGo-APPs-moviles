package com.example.truequego_apps_moviles.features.dashboard

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.truequego_apps_moviles.R
import com.example.truequego_apps_moviles.features.dashboard.MainFeedScreen
import com.example.truequego_apps_moviles.navigation.NavRoutes
import com.example.truequego_apps_moviles.ui.theme.DarkBlue
import com.example.truequego_apps_moviles.ui.theme.GrayText
import com.example.truequego_apps_moviles.ui.theme.OrangeAccent

sealed class BottomNavScreen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val emoji: String
) {
    object Home : BottomNavScreen(NavRoutes.MainFeed.route, "Inicio", Icons.Filled.Home, Icons.Outlined.Home, "🏠")
    object Trades : BottomNavScreen(NavRoutes.Trades.route, "Trueques", Icons.Filled.SwapHoriz, Icons.Outlined.SwapHoriz, "🔄")
    object Alerts : BottomNavScreen(NavRoutes.Alerts.route, "Alertas", Icons.Filled.Notifications, Icons.Outlined.Notifications, "🔔")
    object Profile : BottomNavScreen(NavRoutes.Profile.route, "Perfil", Icons.Filled.Person, Icons.Outlined.Person, "👤")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            // Top Bar con el Logo real y estilo Premium
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground), // REEMPLAZA CON: R.drawable.logo_truequego
                            contentDescription = "Logo",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                modifier = Modifier.height(60.dp)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 15.dp,
                modifier = Modifier.height(75.dp)
            ) {
                val screens = listOf(
                    BottomNavScreen.Home,
                    BottomNavScreen.Trades,
                    null, // Espacio para el FAB central
                    BottomNavScreen.Alerts,
                    BottomNavScreen.Profile
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    screens.forEach { screen ->
                        if (screen == null) {
                            Spacer(modifier = Modifier.width(64.dp))
                        } else {
                            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                            
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(28.dp),
                                    tint = if (isSelected) DarkBlue else GrayText
                                )
                                Text(
                                    text = if (isSelected) screen.emoji else screen.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) DarkBlue else GrayText
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            // FAB con efecto de Gradiente y Brillo
            Box(contentAlignment = Alignment.Center, modifier = Modifier.offset(y = 50.dp)) {
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = OrangeAccent.copy(alpha = 0.2f)
                ) {}
                
                FloatingActionButton(
                    onClick = { /* Acción para nuevo trueque */ },
                    shape = CircleShape,
                    containerColor = OrangeAccent,
                    contentColor = Color.White,
                    modifier = Modifier.size(62.dp),
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(10.dp)
                ) {
                    Icon(Icons.Filled.Add, "Nuevo Trueque", modifier = Modifier.size(34.dp))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.MainFeed.route,
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF8FAFB)) // Fondo gris azulado muy suave
        ) {
            composable(NavRoutes.MainFeed.route) {
                MainFeedScreen()
            }
            // Los otros composables se irán añadiendo
            composable(NavRoutes.Trades.route) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Mis Trueques 🔄", fontSize = 24.sp) } }
            composable(NavRoutes.Alerts.route) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Alertas 🔔", fontSize = 24.sp) } }
            composable(NavRoutes.Profile.route) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Mi Perfil 👤", fontSize = 24.sp) } }
        }
    }
}
