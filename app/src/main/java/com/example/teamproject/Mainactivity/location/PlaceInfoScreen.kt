package com.example.teamproject.Mainactivity.location

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.NavRoutes
import com.example.teamproject.register.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.register.user.Repository
import com.example.teamproject.register.user.UserViewModel
import com.example.teamproject.register.user.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun PlaceInfoScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    Log.d("Repository", "Enter PlaceInfoScreen")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    Log.d("Repository", "Enter viemwodel")
    var location_data by remember {
        navViewModel.Location
    }
    Log.d("Repository","전달된 location 정보: ${location_data.Name}")
    //viewmodel의 장소 리스트에서 같은 이름을 가진 장소를 찾음
//    for(location in navViewModel.LocationList){
//        if(location_id == location.ID)
//            location_data = location
//    }

    Column {
        Text(text = "이름: ${location_data.Name}")
        Text(text = "이름: ${location_data.Name}")
        Text(text = "이름: ${location_data.Name}")
        Text(text = "이름: ${location_data.Name}")
        Text(text = "이름: ${location_data.Name}")
        
        Button(onClick = { navController.navigate(NavRoutes.ReviewScreen.route) }) {
            Text(text = "후기")
        }
    }

}