package com.example.teamproject.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.example.teamproject.viewmodel.LocationData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// recompose가 일어나도록 전체적인 수정 필요
// userData, locationData의 정확한 정의 후 수정 필요
// UI 디자인 확정 후 수정 필요
@Composable
fun ReviewScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val data by remember { navViewModel.Location }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("\'${data.Name}\'에 대한 긍정적 리뷰")
        ReviewPosList(data, onPosReviewClick = { index ->
            navViewModel.updatePosReview(0, index)
        })

        Text("\'${data.Name}\'에 대한 부정적 리뷰", modifier = Modifier.padding(top = 40.dp))
        ReviewNegList(data, onNegReviewClick = { index ->
            navViewModel.updateNegReview(0, index)
        })
    }
}

@Composable
fun ReviewPosList(locationData: LocationData, onPosReviewClick: (Int) -> Unit) {
    LazyColumn {
        locationData.PosReview?.let {
            items(it.size) { index ->
                (locationData.PosReview?.get(index) ?: null)?.let {
                    ReviewItem(
                        "리뷰${index + 1}",
                        it,
                        onClick = { onPosReviewClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewNegList(locationData: LocationData, onNegReviewClick: (Int) -> Unit) {
    LazyColumn {
        locationData.NegReview?.let {
            items(it.size) { index ->
                ReviewItem(
                    "리뷰${index + 1}",
                    locationData.NegReview?.get(index) ?: 0,
                    onClick = { onNegReviewClick(index) }
                )
            }
        }
    }
}


@Composable
fun ReviewItem(review: String, counter: Int, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("$review                $counter")
    }
}