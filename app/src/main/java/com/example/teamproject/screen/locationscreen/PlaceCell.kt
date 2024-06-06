package com.example.teamproject.screen.locationscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.viewmodel.LocationData
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun PlaceCell(location_data: LocationData, navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val showButtons = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.height(50.dp).fillMaxWidth()
                .border(width = 4.dp, color = Color.Black)
                .clickable { showButtons.value = !showButtons.value },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = location_data.Name,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        if (showButtons.value) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navViewModel.setLocation(location_data.ID)
                        navController.navigate(NavRoutes.PlaceInfoScreen.route)
                              },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Text(text = "상세 정보 보기")
                }
                Button(
                    onClick = {
                        navViewModel.setLocation(location_data.ID)
                        navController.navigate(NavRoutes.ReviewScreen.route)
                              },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                ) {
                    Text(text = "후기 보기")
                }
            }
        }
    }
}