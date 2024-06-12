package com.example.teamproject.screen.loginscreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserData
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun Register(navController: NavHostController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)))

    var userID by remember { mutableStateOf("") }
    var userPasswd by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d("RegisterViewModel", userViewModel.toString())
        Text(
            text = "회원가입",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )

        OutlinedTextField(
            value = userID,
            onValueChange = { userID = it },
            label = { Text("아이디") }
        )

        OutlinedTextField(
            value = userPasswd,
            onValueChange = { userPasswd = it },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Text(
            text = "7자리 이상 특수 문자를 포함해야 합니다.",
            fontSize = 12.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("닉네임") }
        )

        Button(onClick = {
            if (userViewModel.UserList.any { it.UserId == userID }) {
                dialogMessage = "아이디가 이미 존재합니다."
                showDialog = true
            } else if (userPasswd.length < 7 || !userPasswd.any { !it.isLetterOrDigit() }) {
                dialogMessage = "비밀번호는 7자리 이상이어야 하며, 적어도 하나의 특수 문자를 포함해야 합니다."
                showDialog = true
            } else {
                val newUser = UserData(
                    UserId = userID,
                    UserPw = userPasswd,
                    UserName = userName,
                    isMaster = false,
                    favoriteLocation = mutableListOf(),
                    friendList = mutableListOf(), mutableListOf(), mutableListOf()
                )
                userViewModel.addUser(newUser)
                navController.navigate(NavRoutes.Login.route)
            }
        }) {
            Text("가입하기")
        }

        Button(onClick = {
            navController.navigate(NavRoutes.Login.route)
        }) {
            Text("취소하기")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "오류") },
            text = { Text(text = dialogMessage) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("확인")
                }
            }
        )
    }
}