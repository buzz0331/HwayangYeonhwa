package com.example.teamproject.screen.loginscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teamproject.R
import com.example.teamproject.navigation.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.viewmodel.Repository
import com.example.teamproject.viewmodel.UserViewModel
import com.example.teamproject.viewmodel.UserViewModelFactory
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
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF7EEDF1))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "화양연화",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF3F51B5)
        )
        Text(
            text = "화양동 핫플레이스를 한번에!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF3F51B5)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.main_icon),  // 이미지 리소스 ID
            contentDescription = "중앙 이미지",
            modifier = Modifier.height(200.dp)  // 필요한 경우 크기 조정
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = userID,
            onValueChange = { userID = it },
            label = { Text("아이디") },
            modifier = Modifier.padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            )
        )

        OutlinedTextField(
            value = userPasswd,
            onValueChange = { userPasswd = it },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    Log.d("Repository", "Attempting to log in with ID: $userID")
                    val loginResult = userViewModel.checkInfo(userID, userPasswd)
                    Log.d("Repository", "Login result: $loginResult")

                    if(userViewModel.checkMaster(userID,userPasswd)){
                        navController.navigate(NavRoutes.MainMasterScreen.route)
                    } else if (loginResult) {
                        Log.d("Repository","${userViewModel.UserList}")
                        userViewModel.setUser(userID)
                        navController.navigate(NavRoutes.MainScreen.route)
                    } else {
                        showAlertDialog = true
                    }
                }
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .focusRequester(passwordFocusRequester)
        )

        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                navController.navigate(NavRoutes.Register.route)
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("회원가입")
            }
            Button(onClick = {
                Log.d("Repository", "Attempting to log in with ID: $userID")
                val loginResult = userViewModel.checkInfo(userID, userPasswd)
                Log.d("Repository", "Login result: $loginResult")

                if(userViewModel.checkMaster(userID,userPasswd)){
                    navController.navigate(NavRoutes.MainMasterScreen.route)
                } else if (loginResult) {
                    Log.d("Repository","${userViewModel.UserList}")
                    userViewModel.setUser(userID)
                    navController.navigate(NavRoutes.MainScreen.route)
                } else {
                    showAlertDialog = true
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(8.dp)
            ) {
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
