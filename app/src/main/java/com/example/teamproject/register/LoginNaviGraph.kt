package com.example.teamproject.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teamproject.Mainactivity.BottomNavigationBar
import com.example.teamproject.Mainactivity.MainScreen
import com.example.teamproject.Mainactivity.items.Contacts
import com.example.teamproject.Mainactivity.items.Favorites
import com.example.teamproject.Mainactivity.items.Home
import com.example.teamproject.Mainactivity.location.FavoritesScreen
import com.example.teamproject.Mainactivity.location.ReviewScreen
import com.example.teamproject.NavRoutes

@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { context as ViewModelStoreOwner }
}

val LocalNavGraphViewModelStoreOwner =
    staticCompositionLocalOf<ViewModelStoreOwner> {
        error("Undefined")
    }

@Composable
fun LoginNavGraph(navController: NavHostController) {

    val navStoreOwner = rememberViewModelStoreOwner()

    CompositionLocalProvider(
        LocalNavGraphViewModelStoreOwner provides navStoreOwner
    ) {
        NavHost(navController = navController, startDestination = NavRoutes.Login.route) {
            composable(route = NavRoutes.Login.route) {
                LoginScreen(navController)
            }
            composable(
                route = NavRoutes.Welcome.route
            ) {
                BottomNavigationBar(navController)
            }

            composable(route = NavRoutes.Register.route) { it ->
                Register(navController)
            }
            composable(NavRoutes.Home.route){
                Home()
            }
            composable(NavRoutes.Contacts.route){
                Contacts()
            }
            composable(NavRoutes.Favorites.route){
                Favorites()
            }
            composable(NavRoutes.MainScreen.route){
                MainScreen()
            }
            composable(NavRoutes.ReviewScreen.route){
                ReviewScreen(navController)
            }
            composable(NavRoutes.FavoritesScreen.route){
                FavoritesScreen(navController)
            }
        }
    }
}