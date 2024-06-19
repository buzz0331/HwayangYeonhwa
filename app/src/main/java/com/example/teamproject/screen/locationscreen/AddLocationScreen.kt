package com.example.teamproject.screen.locationscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.viewmodel.LocationData
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
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
    var review by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var menuImageUri by remember { mutableStateOf<Uri?>(null) }
    val categories = listOf("음식점", "카페", "기타")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val menuLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        menuImageUri = uri
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

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

        TextField(
            value = review,
            onValueChange = { review = it },
            label = { Text("장소 설명") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            )
        ) {
            Text("사진 추가")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedCategory == "음식점" || selectedCategory == "카페") {
            Button(
                onClick = { menuLauncher.launch("image/*") },
                modifier = Modifier.align(Alignment.Start),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                )
            ) {
                Text("메뉴 사진 추가")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // 장소 등록 로직
                if (placeName.isNotEmpty() && selectedCategory.isNotEmpty() && location.isNotEmpty() && review.isNotEmpty()) {
                    val newLocation = LocationData(
                        Category = when (selectedCategory) {
                            "음식점" -> 1
                            "카페" -> 2
                            else -> 3
                        },
                        Name = placeName,
                        ID = navViewModel.LocationList.size + 1,
                        isAccepted = false,
                        imageUrl = "",
                        review = review,
                        data = "",
                        PosReview = MutableList(6){mutableListOf("")},
                        NegReview = MutableList(6){mutableListOf("")},
                        Latitude = 0.0,
                        Longitude = 0.0
                    )
                    // 사진 저장 로직 추가
                    imageUri?.let { newLocation.imageUrl = it.toString() }
                    menuImageUri?.let { newLocation.imageUrl += "|${it.toString()}" }

                    navViewModel.addLocation(newLocation)
                    dialogMessage = "장소가 추가되었습니다. 관리장 승인 후 확인할 수 있습니다"
                    placeName = ""
                    selectedCategory = ""
                    location = ""
                    review = ""
                    imageUri = null
                    menuImageUri = null
                } else {
                    dialogMessage = "정보를 전부 입력해주세요"
                }
                showDialog = true
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            )
        ) {
            Text("등록")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("확인")
                    }
                },
                text = { Text(dialogMessage) }
            )
        }
    }
}