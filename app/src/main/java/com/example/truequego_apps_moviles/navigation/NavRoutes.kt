package com.example.truequego_apps_moviles.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object ForgotPassword : NavRoutes("forgot_password")
    
    // Dashboard y secciones principales
    object Dashboard : NavRoutes("dashboard")
    object MainFeed : NavRoutes("main_feed")
    object Trades : NavRoutes("trades")
    object Alerts : NavRoutes("alerts")
    object Profile : NavRoutes("profile")
    object ResetPassword : NavRoutes("reset_password")
    object Publish : NavRoutes("publish")
    object Communities : NavRoutes("communities")
    object Map : NavRoutes("map")
}
