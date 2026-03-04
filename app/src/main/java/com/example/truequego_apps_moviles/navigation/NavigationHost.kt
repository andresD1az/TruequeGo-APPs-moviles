package com.example.truequego_apps_moviles.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.truequego_apps_moviles.features.home.HomeScreen
import com.example.truequego_apps_moviles.features.login.LoginScreen
import com.example.truequego_apps_moviles.features.register.RegisterScreen
import com.example.truequego_apps_moviles.features.recovery.PasswordRecoveryScreen
import com.example.truequego_apps_moviles.features.recovery.PasswordResetScreen
import com.example.truequego_apps_moviles.features.dashboard.DashboardScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onNavigateToLogin = { navController.navigate(NavRoutes.Login.route) }
            )
        }
        composable(NavRoutes.Login.route) {
            LoginScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRegister = { navController.navigate(NavRoutes.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(NavRoutes.ForgotPassword.route) },
                onLoginSuccess = { 
                    navController.navigate(NavRoutes.Dashboard.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.Dashboard.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.ForgotPassword.route) {
            PasswordRecoveryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReset = { navController.navigate(NavRoutes.ResetPassword.route) }
            )
        }
        composable(NavRoutes.ResetPassword.route) {
            PasswordResetScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.Dashboard.route) {
            DashboardScreen()
        }
    }
}
