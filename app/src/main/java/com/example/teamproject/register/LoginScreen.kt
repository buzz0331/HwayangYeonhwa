package com.example.teamproject.register

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teamproject.NavRoutes
import com.example.teamproject.register.user.Repository
import com.example.teamproject.register.user.UserViewModel
import com.example.teamproject.register.user.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavHostController) {

    val table = Firebase.database.getReference("UserDB/Users")
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var userID by remember { mutableStateOf("") }
    var userPasswd by remember { mutableStateOf("") }
    var showAlertDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "화양연화",
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
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Row {
            Button(onClick = {
                navController.navigate(NavRoutes.Register.route)
            }) {
                Text("회원가입")
            }
            Button(onClick = {
                Log.d("Repository", "Attempting to log in with ID: $userID")
                val loginResult = userViewModel.checkInfo(userID, userPasswd)
                Log.d("Repository", "Login result: $loginResult")

                if(userViewModel.checkMaster(userID,userPasswd)){
                    navController.navigate(NavRoutes.MasterScreen.route)
                } else if (loginResult) {
                    Log.d("Repository", "Login result")
                    userViewModel.setUser("user")
                    navController.navigate(NavRoutes.MainScreen.route)
                } else {
                    showAlertDialog = true
                }
            }) {
                Text(text = "로그인")
            }

            if (showAlertDialog) {
                AlertDialog(
                    onDismissRequest = { showAlertDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showAlertDialog = false }) {
                            Text("확인")
                        }
                    },
                    title = { Text("로그인 실패") },
                    text = { Text("아이디 또는 비밀번호가 잘못되었습니다. 다시 시도하세요.") }
                )
            }
        }
    }
}
