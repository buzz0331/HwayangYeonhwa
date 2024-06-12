package com.example.teamproject.navigation

sealed class NavRoutes (val route: String) {
    object Home : NavRoutes("Home")
    object Contacts : NavRoutes("ContactsScreen")
//    object Favorites : NavRoutes("Favorites")
    object Login : NavRoutes("Login")
    object Welcome : NavRoutes("Welcome")
    object Register : NavRoutes("Register")
    object MainScreen: NavRoutes("MainScreen")
    object ReviewScreen: NavRoutes("ReviewScreen")
    object FavoritesScreen: NavRoutes("FavoritesScreen")
    object PlaceInfoScreen: NavRoutes("PlaceInfoScreen")
    object AddLocationScreen: NavRoutes("AddLocationScreen")
    object MasterScreen: NavRoutes("MasterScreen")
    object MasterScreen2: NavRoutes("MasterScreen2")
    object MainMasterScreen: NavRoutes("MainMasterScreen")
    object FriendLocations: NavRoutes("FriendLocations")
}