package com.example.teamproject.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.viewmodel.LocationData
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun ReviewScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val data by remember { navViewModel.Location }
    val locationIndex = navViewModel.LocationList.indexOfFirst { it.ID == data.ID }
    val userID = navViewModel.User.component1().UserId

    var reviewCounter by remember { mutableStateOf(0) }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF6FAFE))) {
        item {
            Text(
                text = "\'${data.Name}\'에 대한 긍정적 리뷰",
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            ReviewPosList(data, reviewCounter, userID, onPosReviewClick = { index ->
                navViewModel.updatePosReview(locationIndex, index, userID)
                reviewCounter++
            })
        }
        item {
            Text(
                text = "\'${data.Name}\'에 대한 부정적 리뷰",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 40.dp)
            )
        }
        item {
            ReviewNegList(data, reviewCounter, userID, onNegReviewClick = { index ->
                navViewModel.updateNegReview(locationIndex, index, userID)
                reviewCounter++
            })
        }
    }
}

@Composable
fun ReviewPosList(locationData: LocationData, reviewCounter: Int, userID: String, onPosReviewClick: (Int) -> Unit) {
    val positiveReviews = when (locationData.Category) {
        1 -> listOf("가성비가 좋아요", "음식이 맛있어요", "친절해요","양이 많아요","서비스가 최고예요","매장이 깨끗해요")
        2 -> listOf("커피가 맛있어요", "분위기가 좋아요", "직원이 친절해요","뷰가 좋아요","인테리어가 멋져요","분위기가 아늑해요")
        3 -> listOf("시설이 깔끔해요", "재미있어요", "친절해요","가성비가 좋아요","놀거리가 많아요","서비스가 최고예요")
        else -> listOf("리뷰1", "리뷰2", "리뷰3", "리뷰4", "리뷰5", "리뷰6")
    }

    locationData.PosReview?.let { posReviews ->
        val indexedReviews = posReviews.mapIndexed { index, review ->
            index to review
        }.sortedByDescending { it.second.size }

        Column {
            indexedReviews.forEach { (originalIndex, review) ->
                val color = if (review.contains(userID)) 2 else 1
                ReviewItem(
                    review = if (originalIndex < positiveReviews.size) positiveReviews[originalIndex] else "리뷰${originalIndex + 1}",
                    counter = review.size,
                    color = color,
                    onClick = { onPosReviewClick(originalIndex) }
                )
            }
        }
    }
}

@Composable
fun ReviewNegList(locationData: LocationData, reviewCounter: Int, userID: String, onNegReviewClick: (Int) -> Unit) {
    val negativeReviews = when (locationData.Category) {
        1 -> listOf("가게가 더러워요", "음식 맛이 이상해요", "서비스가 안좋아요","찾아가기 어려워요","웨이팅이 길어요","실제 존재하지 않는 장소예요")
        2 -> listOf("커피가 맛없어요", "자리가 불편해요", "직원이 불친절해요","너무 시끄러워요","웨이팅이 길어요","실제 존재하지 않는 장소예요")
        3 -> listOf("시설이 더러워요", "비싸요","고장이 많아요", "서비스가 별로예요","웨이팅이 길어요","실제 존재하지 않는 장소예요")
        else -> listOf("리뷰1", "리뷰2", "리뷰3","리뷰4","리뷰5","리뷰6")
    }

    locationData.NegReview?.let { negReviews ->
        val indexedReviews = negReviews.mapIndexed { index, review ->
            index to review
        }.sortedByDescending { it.second.size }

        Column {
            indexedReviews.forEach { (originalIndex, review) ->
                val color = if (review.contains(userID)) 3 else 1
                ReviewItem(
                    review = if (originalIndex < negativeReviews.size) negativeReviews[originalIndex] else "리뷰${originalIndex + 1}",
                    counter = review.size,
                    color = color,
                    onClick = { onNegReviewClick(originalIndex) }
                )
            }
        }
    }
}

@Composable
fun ReviewItem(review: String, counter: Int, color: Int, onClick: () -> Unit) {
    val buttonColor =
        if (color == 2)
            ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7))
        else if (color == 3)
            ButtonDefaults.buttonColors(containerColor = Color(0xFFEF9A9A))
        else
            ButtonDefaults.buttonColors(containerColor = Color(0xFFBBDEFB))

    FilledTonalButton(
        onClick = onClick,
        colors = buttonColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = review, color = Color.Black)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "$counter", color = Color.Black)
                if (color == 2 || color == 3) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}
