package com.example.teamproject.screen

import android.bluetooth.BluetoothA2dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
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
        ReviewPosList(data, reviewCounter, onPosReviewClick = { index ->
            navViewModel.updatePosReview(locationIndex, index, userID)
            reviewCounter++
        })

        Text("\'${data.Name}\'에 대한 부정적 리뷰", modifier = Modifier.padding(top = 40.dp))
        ReviewNegList(data, reviewCounter, onNegReviewClick = { index ->
            navViewModel.updateNegReview(locationIndex, index, userID)
            reviewCounter++
        })
    }
}

@Composable
fun ReviewPosList(locationData: LocationData, reviewCounter: Int, onPosReviewClick: (Int) -> Unit) {
    LazyColumn {
        locationData.PosReview?.let {
            items(it.size) { index ->
                (locationData.PosReview?.get(index) ?: null)?.let {

                    ReviewItem(
                        review =
                        if (index == 0)
                            "가게가 깨끗해요"
                        else if (index == 1)
                            "음식이 맛있어요"
                        else if (index == 2)
                            "직원이 친절해요"
                        else if (index == 3)
                            "리뷰4"
                        else if (index == 4)
                            "리뷰5"
                        else
                            "리뷰6",
                        it.size,
                        onClick = { onPosReviewClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewNegList(locationData: LocationData, reviewCounter: Int, onNegReviewClick: (Int) -> Unit) {
    LazyColumn {
        locationData.NegReview?.let {
            items(it.size) { index ->
                (locationData.NegReview?.get(index) ?: null)?.let {
                    ReviewItem(
                        review =
                        if (index == 0)
                            "가게가 더러워요"
                        else if (index == 1)
                            "음식이 상했어요"
                        else if (index == 2)
                            "서비스가 안좋아요"
                        else if (index == 3)
                            "리뷰4"
                        else if (index == 4)
                            "리뷰5"
                        else
                            "리뷰6",
                        it.size,
                        onClick = { onNegReviewClick(index) }
                    )
                }
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
