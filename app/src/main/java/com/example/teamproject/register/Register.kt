package com.example.teamproject.register
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teamproject.NavRoutes
import com.example.teamproject.register.user.NavViewModel

@Composable
fun Register(navController: NavHostController) {
    val navViewModel: NavViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var userID by remember{
        mutableStateOf(navViewModel.userID)
    }
    var userPasswd by remember{
        mutableStateOf(navViewModel.userPasswd)
    }
    var userName by remember{
        mutableStateOf(navViewModel.userName)
    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Log.d("RegisterViewModel",navViewModel.toString() )
        Text(
            text = "회원가입",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        OutlinedTextField(value = userID?:"",
            onValueChange = {userID =it},
            label = {Text("아이디")}
        )

        OutlinedTextField( value = userPasswd?:"",
            onValueChange = { userPasswd = it },
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedTextField(value = userName?:"",
            onValueChange = {userName =it},
            label = {Text("닉네임")}
        )
        Button(onClick = {
            navViewModel.addUserInfo(userID,userPasswd,userName)
            navController.navigate(NavRoutes.Login.route)
        }) {
            Text("가입하기")
        }
        Button(onClick = {
            navController.navigate(NavRoutes.Login.route)
        }) {
            Text("취소하기")
        }
    }
}