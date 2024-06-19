package com.example.teamproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teamproject.screen.FavoritesScreen
import com.example.teamproject.screen.FriendLocations
import com.example.teamproject.screen.Home
import com.example.teamproject.screen.ReviewScreen
import com.example.teamproject.screen.friend.ContactsScreen
import com.example.teamproject.screen.locationscreen.AddLocationScreen
import com.example.teamproject.screen.locationscreen.PlaceInfoScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavigationHost(navController: NavHostController) {
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
                ContactsScreen(navController)
            }
            composable(NavRoutes.FavoritesScreen.route){
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
            composable(NavRoutes.FriendLocations.route){
                FriendLocations(navController)
            }
        }
    }

}