package com.example.teamproject.screen.friend

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun ContactsScreen(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current
    )

    var showAddFriendDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Button(onClick = { showAddFriendDialog = true }) {
                Text("친구 추가")
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text("요청 받은 친구 리스트", color = Color.Black, style = MaterialTheme.typography.headlineMedium)
            LazyColumn {
                items(navViewModel.receivedFriendRequests) { friendId ->
                    val friend = navViewModel.getUser(friendId)
                    if (friend != null) {
                        FriendRequestCell(
                            friendName = friend.UserName,
                            friendId = friend.UserId,
                            onAcceptClick = {
                                navViewModel.acceptFriendRequest(friend.UserId)
                            },
                            onDeclineClick = {
                                navViewModel.declineFriendRequest(friend.UserId)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("요청 중인 친구 리스트", color = Color.Black, style = MaterialTheme.typography.headlineMedium)
            LazyColumn {
                items(navViewModel.sentFriendRequests) { friendId ->
                    val friend = navViewModel.getUser(friendId)
                    if (friend != null) {
                        PendingFriendRequestCell(
                            friendName = friend.UserName,
                            friendId = friend.UserId,
                            onCancelRequestClick = {
                                navViewModel.cancelFriendRequest(friend.UserId)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("친구 리스트", color = Color.Black, style = MaterialTheme.typography.headlineMedium)
            LazyColumn {
                items(navViewModel.friendList) { friend ->
                    FriendCell(
                        friendName = friend.UserName,
                        onFavoriteLocationsClick = {
                            navViewModel.setFriend(friend)
                            navController.navigate(NavRoutes.FriendLocations.route)
                        },
                        onDeleteFriendClick = { navViewModel.deleteFriend(friend.UserId) }
                    )
                }
            }
        }

        if (showAddFriendDialog) {
            AddFriendDialog(navViewModel) { showAddFriendDialog = false }
        }
    }
}


@Composable
fun FriendRequestCell(
    friendName: String,
    friendId: String,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit
) {
    var showButtons by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showButtons = !showButtons }
            .padding(vertical = 4.dp)
            .border(width = 2.dp, color = Color.Black)
            .padding(8.dp)
    ) {
        Text(text = "$friendName ($friendId)")
        if (showButtons) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onAcceptClick) {
                    Text("수락")
                }
                Button(onClick = onDeclineClick) {
                    Text("거절")
                }
            }
        }
    }
}

@Composable
fun PendingFriendRequestCell(
    friendName: String,
    friendId: String,
    onCancelRequestClick: () -> Unit
) {
    var showButtons by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showButtons = !showButtons }
            .padding(vertical = 4.dp)
            .border(width = 2.dp, color = Color.Black)
            .padding(8.dp)
    ) {
        Text(text = "$friendName ($friendId)")
        if (showButtons) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onCancelRequestClick) {
                    Text("요청 취소")
                }
            }
        }
    }
}

@Composable
fun AddFriendDialog(navViewModel: UserViewModel, onDismiss: () -> Unit) {
    var friendId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("친구 추가") },
        text = {
            Column {
                TextField(
                    value = friendId,
                    onValueChange = { friendId = it },
                    label = { Text("친구 ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = Color.Red)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        friendId == navViewModel.User.value.UserId -> {
                            errorMessage = "자기 자신은 친구로 추가할 수 없습니다."
                        }
                        navViewModel.User.value.friendList?.contains(friendId) == true -> {
                            errorMessage = "이미 존재하는 친구입니다."
                        }
                        else -> {
                            val friend = navViewModel.getUser(friendId)
                            if (friend != null) {
                                navViewModel.sendFriendRequest(friendId)
                                onDismiss()
                            } else {
                                errorMessage = "친구가 존재하지 않습니다."
                            }
                        }
                    }
                }
            ) {
                Text("추가")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("취소")
            }
        }
    )
}