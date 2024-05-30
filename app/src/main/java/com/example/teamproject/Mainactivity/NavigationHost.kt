package com.example.teamproject.Mainactivity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teamproject.Mainactivity.items.Home
import com.example.teamproject.Mainactivity.location.FavoritesScreen
import com.example.teamproject.Mainactivity.location.ReviewScreen
import com.example.teamproject.NavRoutes

@Composable
fun NagivationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ){
        composable(NavRoutes.Home.route){
            Home()
        }
        composable(NavRoutes.Contacts.route){
            ReviewScreen(navController)
        }
        composable(NavRoutes.Favorites.route){
            FavoritesScreen(navController)
        }

    }
}