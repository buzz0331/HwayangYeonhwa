package com.example.teamproject.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.screen.locationscreen.PlaceList
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.height(300.dp)){
            //map 놓는 곳
        }
        Button(onClick = { navController.navigate(NavRoutes.AddLocationScreen.route) }) {
            Text(text = "장소 추가하기")
        }
        PlaceList(navController = navController)
    }
}