package com.example.teamproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teamproject.master.MainMasterScreen
import com.example.teamproject.master.MasterScreen
import com.example.teamproject.master.MasterScreen2
import com.example.teamproject.screen.loginscreen.LoginScreen
import com.example.teamproject.screen.loginscreen.Register
import com.example.teamproject.screen.mainscreen.BottomNavigationBar
import com.example.teamproject.screen.mainscreen.MainScreen

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
    ){
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
//        composable(NavRoutes.Home.route){
//            Home(navController)
//        }
//        composable(NavRoutes.Contacts.route){
//            Contacts()
//        }
//        composable(NavRoutes.Favorites.route){
//            Favorites()
//        }
            composable(NavRoutes.MainScreen.route){
                MainScreen()
            }
            composable(NavRoutes.MasterScreen.route){
                MasterScreen(navController)
            }
            composable(NavRoutes.MasterScreen2.route){
                MasterScreen2(navController)
            }
            composable(NavRoutes.MainMasterScreen.route){
                MainMasterScreen(navController)
            }
//        composable(NavRoutes.ReviewScreen.route){
//            ReviewScreen(navController)
//        }
//        composable(NavRoutes.FavoritesScreen.route){
//            FavoritesScreen(navController)
//        }
        }
    }


}