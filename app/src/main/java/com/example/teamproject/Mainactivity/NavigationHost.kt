package com.example.teamproject.Mainactivity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teamproject.Mainactivity.items.Contacts
import com.example.teamproject.Mainactivity.items.Favorites
import com.example.teamproject.Mainactivity.items.Home
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
            Contacts()
        }
        composable(NavRoutes.Favorites.route){
            Favorites()
        }
    }
}