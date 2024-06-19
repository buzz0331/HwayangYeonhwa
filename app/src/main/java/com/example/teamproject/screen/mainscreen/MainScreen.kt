package com.example.teamproject.screen.mainscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.navigation.NavigationHost

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navControllerr: NavController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route ?: NavRoutes.Home.route
    val title = when (currentRoute) {
        NavRoutes.Home.route -> "메인 화면"
        NavRoutes.PlaceInfoScreen.route -> "장소 상세보기"
        NavRoutes.ReviewScreen.route -> "후기 보기"
        NavRoutes.AddLocationScreen.route -> "장소 추가하기"
        NavRoutes.Contacts.route -> "친구 목록"
        NavRoutes.FavoritesScreen.route -> "찜한 장소 목록"
        else -> "메인 화면"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title, color = Color(0xFF3F51B5)) },
                navigationIcon = {
                    if (currentRoute != NavRoutes.Home.route) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기", tint = Color(0xFF3F51B5))
                        }
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            navControllerr.navigate(NavRoutes.Login.route){
                                popUpTo(NavRoutes.MainScreen.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F51B5),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "로그아웃")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFF6FAFE)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = Color(0xF7EEDF1)
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            NavigationHost(navController = navController)
        }
    }
}
