package com.example.teamproject.screen.friend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FriendCell(friendName: String, onFavoriteLocationsClick: () -> Unit, onDeleteFriendClick: () -> Unit) {
    val showButtons = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.Black)
                .clickable { showButtons.value = !showButtons.value },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = friendName,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
        if (showButtons.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onFavoriteLocationsClick,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Text(text = "저장한 장소 리스트")
                }
                Button(
                    onClick = onDeleteFriendClick,
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                ) {
                    Text(text = "친구 삭제")
                }
            }
        }
    }
}
