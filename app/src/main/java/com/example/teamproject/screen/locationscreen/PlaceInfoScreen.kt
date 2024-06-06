package com.example.teamproject.screen.locationscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "장소 이름: ${location_data.Name}", color = Color.Black)
        Text(text = "카테고리: $categoryText", color = Color.Black)

        val imageUrls = location_data.imageUrl.split("|")
        if (imageUrls.isNotEmpty() && imageUrls[0].isNotEmpty()) {
            val painter: Painter = rememberAsyncImagePainter(model = imageUrls[0])
            Image(
                painter = painter,
                contentDescription = "장소 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(text = "장소 설명: ${location_data.review}", color = Color.Black)

        if (imageUrls.size > 1 && imageUrls[1].isNotEmpty()) {
            Text(text = "메뉴", color = Color.Black)
            val menuPainter: Painter = rememberAsyncImagePainter(model = imageUrls[1])
            Image(
                painter = menuPainter,
                contentDescription = "메뉴 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Button(onClick = { navController.navigate(NavRoutes.ReviewScreen.route) }) {
            Text(text = "후기")
        }
    }
}
