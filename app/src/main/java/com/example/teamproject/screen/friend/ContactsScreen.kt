package com.example.teamproject.screen.friend

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF6FAFE))) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = { showAddFriendDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                ),
                modifier = Modifier.align(Alignment.Start)) {
                Text("친구 추가")
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text("요청 받은 친구 리스트", color = Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp)
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

            Spacer(modifier = Modifier.height(20.dp))

            Text("요청 중인 친구 리스트", color = Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp)
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

            Spacer(modifier = Modifier.height(20.dp))

            Text("친구 리스트", color = Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp)
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
            .padding(vertical = 8.dp)
    ) {
        Box(modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(25.dp))
            .shadow(4.dp, shape = RoundedCornerShape(25.dp))
            .clickable { showButtons = !showButtons },
            contentAlignment = Alignment.Center){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Friend Icon",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$friendName ($friendId)",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        if (showButtons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onAcceptClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(text = "수락")
                }
                Button(
                    onClick = onDeclineClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(text = "거절")
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
            .padding(vertical = 8.dp)
    ) {
        Box(modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(25.dp))
            .shadow(4.dp, shape = RoundedCornerShape(25.dp))
            .clickable { showButtons = !showButtons },
            contentAlignment = Alignment.Center){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Friend Icon",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$friendName ($friendId)",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        if (showButtons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onCancelRequestClick,
                    modifier = Modifier
                        .width(350.dp)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(text = "요청 취소")
                }
            }
        }
    }
}

@Composable
fun AddFriendDialog(navViewModel: UserViewModel, onDismiss: () -> Unit) {
    var friendId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isFriendValid by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("친구 추가") },
        text = {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = friendId,
                        onValueChange = {
                            friendId = it
                            isFriendValid = false // 입력이 변경될 때마다 유효성 초기화
                        },
                        label = { Text("친구 ID") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        when {
                            friendId == "master" -> {
                                errorMessage = "추가할 수 없는 친구입니다."
                                isFriendValid = false
                            }
                            friendId == navViewModel.User.value.UserId -> {
                                errorMessage = "자기 자신은 친구로 추가할 수 없습니다."
                                isFriendValid = false
                            }
                            navViewModel.User.value.friendList?.contains(friendId) == true -> {
                                errorMessage = "이미 존재하는 친구입니다."
                                isFriendValid = false
                            }
                            navViewModel.sentFriendRequests.contains(friendId) -> {
                                errorMessage = "현재 요청중인 친구의 ID입니다."
                                isFriendValid = false
                            }
                            navViewModel.getUserfromUserList(friendId) != null -> {
                                errorMessage = "친구 추가가 가능한 친구 ID입니다."
                                isFriendValid = true
                            }
                            else -> {
                                errorMessage = "친구가 존재하지 않습니다."
                                isFriendValid = false
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "검색")
                    }
                }
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = if (isFriendValid) Color.Green else Color.Red)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isFriendValid) {
                        navViewModel.sendFriendRequest(friendId)
                        onDismiss()
                    } else {
                        errorMessage = "친구 ID를 먼저 검색해 주세요."
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

