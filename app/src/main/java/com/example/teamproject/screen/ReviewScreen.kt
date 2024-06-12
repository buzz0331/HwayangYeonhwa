package com.example.teamproject.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun ReviewScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val data by remember { navViewModel.Location }
    val locationIndex = navViewModel.LocationList.indexOfFirst { it.ID == data.ID }
    val userID = navViewModel.User.component1().UserId

    var reviewCounter by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("\'${data.Name}\'에 대한 긍정적 리뷰")
        ReviewPosList(data, reviewCounter, userID, onPosReviewClick = { index ->
            navViewModel.updatePosReview(locationIndex, index, userID)
            reviewCounter++
        })

        Text("\'${data.Name}\'에 대한 부정적 리뷰", modifier = Modifier.padding(top = 40.dp))
        ReviewNegList(data, reviewCounter, userID, onNegReviewClick = { index ->
            navViewModel.updateNegReview(locationIndex, index, userID)
            reviewCounter++
        })
    }
}

@Composable
fun ReviewPosList(locationData: LocationData, reviewCounter: Int, userID: String, onPosReviewClick: (Int) -> Unit) {
    var color: Int

    locationData.PosReview?.let { posReviews ->
        val indexedReviews = posReviews.mapIndexed { index, review ->
            index to review
        }.sortedByDescending { it.second.size }

        LazyColumn {
            items(indexedReviews.size) { sortedIndex ->
                val (originalIndex, review) = indexedReviews[sortedIndex]
                if (review.contains(userID))
                    color = 2
                else
                    color = 1

                ReviewItem(
                    review =
                    if (originalIndex == 0)
                        "가게가 더러워요"
                    else if (originalIndex == 1)
                        "음식이 상했어요"
                    else if (originalIndex == 2)
                        "서비스가 안좋아요"
                    else if (originalIndex == 3)
                        "리뷰4"
                    else if (originalIndex == 4)
                        "리뷰5"
                    else
                        "리뷰6",
                    review.size,
                    color,
                    onClick = { onPosReviewClick(originalIndex) }
                )
            }
        }
    }
}

@Composable
fun ReviewNegList(locationData: LocationData, reviewCounter: Int, userID: String, onNegReviewClick: (Int) -> Unit) {
    var color: Int

    // NegReview가 null이 아니고 요소가 있는 경우 처리
    locationData.NegReview?.let { negReviews ->
        // 인덱스와 함께 요소를 리스트로 만든 후, 크기 순으로 정렬
        val indexedReviews = negReviews.mapIndexed { index, review ->
            index to review
        }.sortedByDescending { it.second.size }

        LazyColumn {
            items(indexedReviews.size) { sortedIndex ->
                val (originalIndex, review) = indexedReviews[sortedIndex]
                if (review.contains(userID))
                    color = 3
                else
                    color = 1

                ReviewItem(
                    review =
                    if (originalIndex == 0)
                        "가게가 더러워요"
                    else if (originalIndex == 1)
                        "음식이 상했어요"
                    else if (originalIndex == 2)
                        "서비스가 안좋아요"
                    else if (originalIndex == 3)
                        "리뷰4"
                    else if (originalIndex == 4)
                        "리뷰5"
                    else
                        "리뷰6",
                    review.size,
                    color,
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
            ButtonDefaults.buttonColors(containerColor = Color.Green)
        else if (color == 3)
            ButtonDefaults.buttonColors(containerColor = Color.Red)
        else
            ButtonDefaults.buttonColors()
    FilledTonalButton(
        onClick = onClick,
        colors = buttonColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$review                $counter")
            if (color == 2) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check",
                    modifier = Modifier.size(24.dp)
                )
            }
            if (color == 3) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
