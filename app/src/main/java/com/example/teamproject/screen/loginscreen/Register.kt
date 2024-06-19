package com.example.teamproject.screen.loginscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
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
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var userID by remember { mutableStateOf("") }
    var userPasswd by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var idCheckMessage by remember { mutableStateOf("") }
    var nameCheckMessage by remember { mutableStateOf("") }
    var isIdChecked by remember { mutableStateOf(false) }
    var isNameChecked by remember { mutableStateOf(false) }

    val passwdFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF7EEDF1))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d("RegisterViewModel", userViewModel.toString())
        Text(
            text = "회원가입",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF3F51B5)  // New color for title
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = userID,
                onValueChange = {
                    userID = it
                    isIdChecked = false
                    idCheckMessage = ""
                },
                label = { Text("아이디") },
                keyboardActions = KeyboardActions(
                    onNext = { passwdFocusRequester.requestFocus() }
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(
                onClick = {
                    isIdChecked = true
                    idCheckMessage = if (userViewModel.isIdAvailable(userID)) {
                        "사용 가능한 아이디입니다."
                    } else {
                        "중복되는 아이디가 존재합니다."
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                )
            ) {
                Text("중복 확인")
            }
        }
        Text(
            text = idCheckMessage,
            color = if (idCheckMessage.contains("사용 가능한")) Color.Green else Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userPasswd,
            onValueChange = { userPasswd = it },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .focusRequester(passwdFocusRequester)
        )

        Text(
            text = "7자리 이상 특수 문자를 포함해야 합니다.",
            fontSize = 12.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it
                    isNameChecked = false
                    nameCheckMessage = ""
                },
                label = { Text("닉네임") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(
                onClick = {
                    isNameChecked = true
                    nameCheckMessage = if (userViewModel.isNicknameAvailable(userName)) {
                        "사용 가능한 닉네임입니다."
                    } else {
                        "중복되는 닉네임이 존재합니다."
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                )
            ) {
                Text("중복 확인")
            }
        }
        Text(
            text = nameCheckMessage,
            color = if (nameCheckMessage.contains("사용 가능한")) Color.Green else Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (userViewModel.UserList.any { it.UserId == userID }) {
                        dialogMessage = "아이디가 이미 존재합니다."
                        showDialog = true
                    } else if (userPasswd.length < 7 || !userPasswd.any { !it.isLetterOrDigit() }) {
                        dialogMessage = "비밀번호는 7자리 이상이어야 하며, 적어도 하나의 특수 문자를 포함해야 합니다."
                        showDialog = true
                    } else if (!isIdChecked || !isNameChecked || idCheckMessage.contains("중복") || nameCheckMessage.contains("중복")) {
                        dialogMessage = "아이디와 닉네임의 중복 확인을 완료해 주세요."
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
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("가입하기")
            }

            Button(
                onClick = {
                    navController.navigate(NavRoutes.Login.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("취소하기")
            }
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
