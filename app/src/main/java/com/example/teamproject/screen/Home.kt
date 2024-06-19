package com.example.teamproject.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.screen.locationscreen.PlaceList
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.MarkerState

@Composable
fun Home(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current
    )

    val context = LocalContext.current
    val konkuk = LatLng(37.5408, 127.0793)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom((konkuk), 16f)
    }
    val locations = navViewModel.LocationList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAFE))
    ) {
        Column(modifier = Modifier.height(300.dp)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                locations.forEach { location ->
                    val position = LatLng(location.Latitude,location.Longitude)
                    position?.let { MarkerState(position = it) }?.let {
                        Marker(
                            state = it,
                            title = location.Name
                        )
                    }
                }
                    Marker(
                        state = MarkerState(position = konkuk),
                        title = "건국대학교"
                    )
                }
            }
            Button(
                onClick = { navController.navigate(NavRoutes.AddLocationScreen.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "장소 추가하기")
            }
            PlaceList(navController = navController, 0)
        }
    }

