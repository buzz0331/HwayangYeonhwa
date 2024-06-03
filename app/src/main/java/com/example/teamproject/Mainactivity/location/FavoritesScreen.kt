package com.example.teamproject.Mainactivity.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.NavRoutes
import com.example.teamproject.register.user.Repository
import com.example.teamproject.register.user.UserData
import com.example.teamproject.register.user.UserViewModel
import com.example.teamproject.register.user.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun FavoritesScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)))

    val userData by remember {
        mutableStateOf(navViewModel.UserList[0]) // 수정필요
    }

    Column (modifier = Modifier
        .fillMaxWidth()){

        Text("${userData.UserName}님의 선호 장소 목록")
        FavoritesList(userData) {locationData->
            navController.navigate(NavRoutes.Home.route)

        }
    }
}

@Composable
fun FavoritesList(userData:UserData, onItemClick: (LocationData) -> Unit) {
    LazyColumn {
        items(userData.favoriteLocation.size){index->
            Text("${index+1}.")
            FavoritesLocation(userData.favoriteLocation[index], onItemClick)
        }
    }
}

@Composable
fun FavoritesLocation(locationData: LocationData, onItemClick: (LocationData) -> Unit) {
    Column {
        Text("가게 이름: ${locationData.Name}")

        if(locationData.Category == 1)
            Text("가게 종류: 음식점")
        else if(locationData.Category == 2)
            Text("가게 종류: 카페")
        else
            Text("가게 종류: 오락")

        Row (modifier = Modifier
            .fillMaxWidth()){
            Button(onClick = {onItemClick(locationData)}) {
                Text("세부정보 보러가기")
            }
            Button(onClick = {onItemClick(locationData)}) {
                Text("가게 후기 보러가기")
            }
        }
    }
}

