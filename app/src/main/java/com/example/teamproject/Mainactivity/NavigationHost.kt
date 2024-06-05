package com.example.teamproject.Mainactivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teamproject.Mainactivity.items.Contacts
import com.example.teamproject.Mainactivity.items.Home
import com.example.teamproject.Mainactivity.location.AddLocationScreen
import com.example.teamproject.Mainactivity.location.FavoritesScreen
import com.example.teamproject.Mainactivity.location.PlaceInfoScreen
import com.example.teamproject.Mainactivity.location.ReviewScreen
import com.example.teamproject.NavRoutes
import com.example.teamproject.register.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.register.rememberViewModelStoreOwner


@Composable
fun NagivationHost(navController: NavHostController) {
    val navStoreOwner = rememberViewModelStoreOwner()
    CompositionLocalProvider(
        LocalNavGraphViewModelStoreOwner provides navStoreOwner
    ){
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route
        ){
            composable(NavRoutes.Home.route){
                Home(navController)
            }
            composable(NavRoutes.Contacts.route){
                Contacts(navController)
            }
            composable(NavRoutes.Favorites.route){
                FavoritesScreen(navController)
            }
            composable(NavRoutes.PlaceInfoScreen.route){
                PlaceInfoScreen(navController)
            }
            composable(NavRoutes.ReviewScreen.route){
                ReviewScreen(navController)
            }
            composable(NavRoutes.AddLocationScreen.route){
                AddLocationScreen(navController)
            }
        }
    }

}