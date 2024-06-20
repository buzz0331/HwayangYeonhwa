package com.example.teamproject.screen.locationscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun PlaceInfoScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    Log.d("Repository", "Enter PlaceInfoScreen")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    Log.d("Repository", "Enter viemwodel")
    val location_data by remember {
        navViewModel.Location
    }
    Log.d("Repository","전달된 location 정보: ${location_data.Name}")

    val categoryText = when (location_data.Category) {
        1 -> "음식점"
        2 -> "카페"
        3 -> "기타"
        else -> "기타"
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogImageUrl by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(16.dp))
            .background(Color(0xF7EEDF1))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "($categoryText) ${location_data.Name}",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val imageUrls = location_data.imageUrl.split("|")
        if (imageUrls.isNotEmpty() && imageUrls[0].isNotEmpty()) {
            val painter: Painter = rememberAsyncImagePainter(model = imageUrls[0])
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clickable {
                        dialogImageUrl = imageUrls[0]
                        showDialog = true
                    }
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "장소 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Use Crop to make the image fill the container while preserving aspect ratio
                )
            }
        }

        Text(
            text = "장소 설명: ${location_data.review}",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (imageUrls.size > 1 && imageUrls[1].isNotEmpty()) {
            Text(text = "메뉴", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            val menuPainter: Painter = rememberAsyncImagePainter(model = imageUrls[1])
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clickable {
                        dialogImageUrl = imageUrls[1]
                        showDialog = true
                    }
                    .background(Color.Gray)
            ) {
                Image(
                    painter = menuPainter,
                    contentDescription = "메뉴 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Use Crop to make the image fill the container while preserving aspect ratio
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { navController.navigate(NavRoutes.ReviewScreen.route) }) {
                Text(text = "후기")
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = { },
            text = {
                ImageDialogContent(imageUrl = dialogImageUrl, onDismiss = { showDialog = false })
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)  // 높이를 화면의 80%로 설정
        )
    }
}

@Composable
fun ImageDialogContent(imageUrl: String, onDismiss: () -> Unit) {
    val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)  // 이미지 높이 조절
            .background(Color.Black)
            .clickable { onDismiss() },
        contentScale = ContentScale.Fit // Use Fit to make the image fill the container while preserving aspect ratio
    )
}
