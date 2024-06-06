package com.example.teamproject.screen.friend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun ContactsScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var showAddFriendDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "contacts",
                tint = Color.Blue,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { showAddFriendDialog = true }) {
                Text("친구 추가")
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(navViewModel.friendList) { friend ->
                    FriendCell(
                        friendName = friend.UserName,
                        onFavoriteLocationsClick = {
                            navViewModel.setFriend(friend)
                            navController.navigate(NavRoutes.FriendLocations.route)
                        },
                        onDeleteFriendClick = { /* 친구 삭제 로직 추가 */ }
                    )
                }
            }
        }

        if (showAddFriendDialog) {
            AlertDialog(
                onDismissRequest = { showAddFriendDialog = false },
                title = { Text("친구 추가") },
                text = { Text("친구를 추가하는 기능을 구현합니다.") },
                confirmButton = {
                    TextButton(onClick = { showAddFriendDialog = false }) {
                        Text("확인")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddFriendDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}
