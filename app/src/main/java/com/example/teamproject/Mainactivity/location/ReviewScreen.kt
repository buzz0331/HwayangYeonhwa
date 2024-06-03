package com.example.teamproject.Mainactivity.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.register.user.Repository
import com.example.teamproject.register.user.UserViewModel
import com.example.teamproject.register.user.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// recompose가 일어나도록 전체적인 수정 필요
// userData, locationData의 정확한 정의 후 수정 필요
// UI 디자인 확정 후 수정 필요
@Composable
fun ReviewScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)))
    val locationData by remember {
        mutableStateOf((navViewModel.LocationList))
    }

    val data by remember { mutableStateOf(navViewModel.LocationList[0]) }

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
        items(locationData.PosReview.size) { index ->
            ReviewItem(
                "리뷰${index + 1}",
                locationData.PosReview[index],
                onClick = { onPosReviewClick(index) }
            )
        }
    }
}

@Composable
fun ReviewNegList(locationData: LocationData, onNegReviewClick: (Int) -> Unit) {
    LazyColumn {
        items(locationData.NegReview.size) { index ->
            ReviewItem(
                "리뷰${index + 1}",
                locationData.NegReview[index],
                onClick = { onNegReviewClick(index) }
            )
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