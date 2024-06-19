package com.example.teamproject.screen.locationscreen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.viewmodel.LocationData
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@ExperimentalPermissionsApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var placeName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<LatLng?>(null)}
    var latitude by remember { mutableStateOf("")}
    var longitude by remember { mutableStateOf("")}
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

    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions =
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    val finePermission =
        permissionState.permissions[0].status.isGranted
    val coarsePermission =
        permissionState.permissions[1].status.isGranted

    LaunchedEffect(Unit) {
        if (finePermission || coarsePermission) {
            val fetchLocation = { pos: LatLng? -> location = pos }
            getCurrentLocation(fusedLocationClient, fetchLocation)
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
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
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("위치(위도)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("위치(경도)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (location != null) {
                    latitude = location!!.latitude.toString()
                    longitude = location!!.longitude.toString()
                } else {
                    latitude = "0"
                    longitude = "0"
                }
            },
            modifier = Modifier.align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            )
        ) {
            Text("현재 위치 불러오기")
        }

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
                if (placeName.isNotEmpty() && selectedCategory.isNotEmpty() && latitude.isNotEmpty() && longitude.isNotEmpty() && review.isNotEmpty()) {
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
                        PosReview = MutableList(6) { mutableListOf("") },
                        NegReview = MutableList(6) { mutableListOf("") },
                        Location = LatLng(latitude.toDouble(), longitude.toDouble())
                    )
                    // 사진 저장 로직 추가
                    imageUri?.let { newLocation.imageUrl = it.toString() }
                    menuImageUri?.let { newLocation.imageUrl += "|${it.toString()}" }

                    navViewModel.addLocation(newLocation)
                    dialogMessage = "장소가 추가되었습니다. 관리장 승인 후 확인할 수 있습니다"
                    placeName = ""
                    selectedCategory = ""
                    latitude = ""
                    longitude = ""
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

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    fetchLocation: (LatLng?) -> Unit
) {
    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).addOnSuccessListener {
        if (it != null)
            fetchLocation(LatLng(it.latitude, it.longitude))
        else
            fetchLocation(null)
    }
}
