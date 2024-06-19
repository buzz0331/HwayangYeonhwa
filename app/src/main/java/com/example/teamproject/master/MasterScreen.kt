package com.example.teamproject.master

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val locations by remember { mutableStateOf(userViewModel.LocationList) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(text = "장소 승인창", fontSize = 24.sp, color = Color.Black)
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(NavRoutes.MainMasterScreen.route) {
                        popUpTo(NavRoutes.MainMasterScreen.route) { inclusive = true }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(locations.filter { !it.isAccepted }) { location ->
                ExpandableItemCard(location,
                    onApprove = { loc, additionalData ->
                        userViewModel.approveLocation(loc, additionalData)
                        navController.navigate("MainMasterScreen") { popUpTo("MainMasterScreen") { inclusive = true } }
                    },
                    onReject = { loc ->
                        userViewModel.rejectLocation(loc)
                        navController.navigate("MainMasterScreen") { popUpTo("MainMasterScreen") { inclusive = true } }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableItemCard(
    location: LocationData,
    onApprove: (LocationData, String) -> Unit,
    onReject: (LocationData) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var additionalData by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = location.Name, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = location.review, color = Color.Gray)

            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    TextField(
                        value = additionalData,
                        onValueChange = { additionalData = it },
                        label = { Text("가게에 대한 정보입력") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                onApprove(location, additionalData)
                                expanded = false
                            }
                        ) {
                            Text(text = "승인하기")
                        }
                        Button(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = {
                                onReject(location)
                                expanded = false
                            }
                        ) {
                            Text(text = "거절하기")
                        }
                    }
                }
            }
        }
    }
}