package com.example.teamproject.Mainactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamproject.Mainactivity.location.LocationData
import com.example.teamproject.register.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.register.user.Repository
import com.example.teamproject.register.user.UserViewModel
import com.example.teamproject.register.user.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun MasterScreen() {
    val itemList = remember { mutableStateListOf(
        Item(1, "돕", "점시에는 국밥류 저녁식사에는 감자탕을 맛있게 먹을 수 있어요" +
                "특히 해장할때 추천합니다!!"),
        Item(2, "피터팬", "노래주점인데 술집과 노래방을 동시에 즐길수 있어요!!"),
        Item(3, "메가커피", "건대 중문쪽에 있으며 저렴한 가격에 커피한잔을 마실수 있어요")
    )}

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "관리자 승인 창",
            fontSize = 24.sp,
            color = Color.Black
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(itemList) { item ->
                ExpandableItemCard(item) { removedItem ->
                    // Remove the item from the list when approved or rejected
                    itemList.remove(removedItem)
                }
            }
        }
    }
}

data class Item(val id: Int, val name: String, val review: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableItemCard(item: Item, onItemRemoved: (Item) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val table = Firebase.database.getReference("UserDB/Users")
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.name, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.review, color = Color.Gray)

            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                userViewModel.LocationList.add(
                                    LocationData(
                                        item.id,
                                        item.name,
                                        userViewModel.LocationList.lastIndex + 1,true,
                                        mutableListOf(0, 0, 0, 0, 0, 0),
                                        mutableListOf(0, 0, 0, 0, 0, 0)
                                    )
                                )
                                onItemRemoved(item)

                                expanded = false
                            }
                        ) {
                            Text(text = "승인하기")
                        }
                        Button(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = {
                                onItemRemoved(item)
                                expanded = false
                            }
                        ) {
                            Text(text = "거절하기")
                        }
                    }
                }
            }
        }
    }
}