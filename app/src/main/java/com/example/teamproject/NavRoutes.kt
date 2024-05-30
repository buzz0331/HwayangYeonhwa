package com.example.teamproject

sealed class NavRoutes (val route: String) {
    object Home : NavRoutes("Home")
    object Contacts : NavRoutes("Contacts")
    object Favorites : NavRoutes("Favorites")
    object Login : NavRoutes("Login")
    object Welcome : NavRoutes("Welcome")
    object Register : NavRoutes("Register")
    object MainScreen: NavRoutes("MainScreen")
    object ReviewScreen: NavRoutes("ReviewScreen")
    object FavoritesScreen: NavRoutes("FavoritesScreen")
}