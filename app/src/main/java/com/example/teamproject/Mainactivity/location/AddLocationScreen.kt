package com.example.teamproject.Mainactivity.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.register.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.register.user.Repository
import com.example.teamproject.register.user.UserViewModel
import com.example.teamproject.register.user.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var placeName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val categories = listOf("음식점", "카페", "기타")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("장소 등록")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = placeName,
            onValueChange = { placeName = it },
            label = { Text("장소 이름") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("카테고리")
        categories.forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp)
                    .clickable { selectedCategory = category }
            ) {
                RadioButton(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(category)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("위치") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // 장소 등록 로직
//                      navViewModel.
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("등록")
        }
    }
}