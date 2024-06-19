package com.example.teamproject.screen.locationscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

    val isFavorite = remember {
        mutableStateOf(navViewModel.User.value.favoriteLocation?.any { it.ID == location_data.ID } == true)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .height(40.dp)
                .fillMaxWidth()
                .graphicsLayer {
                    shadowElevation = 8.dp.toPx()
                    shape = RoundedCornerShape(25.dp)
                    clip = true
                }
                .background(Color(0xFFDDE2FF), shape = RoundedCornerShape(25.dp))
                .clickable { showButtons.value = !showButtons.value },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = location_data.Name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color(0xFF3F51B5)
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = if (isFavorite.value) Color.Yellow else Color(0xFF3F51B5),
                    contentDescription = if (isFavorite.value) "Favorite" else "Not Favorite",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            if (isFavorite.value) {
                                navViewModel.removeFavoriteLocation(location_data)
                            } else {
                                navViewModel.addFavoriteLocation(location_data)
                            }
                            isFavorite.value = !isFavorite.value
                        }
                )
            }
        }
        if (showButtons.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navViewModel.setLocation(location_data.ID)
                        navController.navigate(NavRoutes.PlaceInfoScreen.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(text = "상세 정보 보기")
                }
                Button(
                    onClick = {
                        navViewModel.setLocation(location_data.ID)
                        navController.navigate(NavRoutes.ReviewScreen.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(text = "후기 보기")
                }
            }
        }
    }
}
