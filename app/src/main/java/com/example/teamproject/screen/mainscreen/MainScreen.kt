package com.example.teamproject.screen.mainscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.teamproject.navigation.NavRoutes
import com.example.teamproject.navigation.NavigationHost

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
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
                title = { Text(text = title) },
                navigationIcon = {
                    if (currentRoute != NavRoutes.Home.route) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // 로그아웃 로직 추가
                    }, modifier = Modifier.width(100.dp)) {
                        Text(text = "로그아웃", color = Color.Blue)
                    }
                },
                modifier = Modifier.background(Color(0xFF6200EA)) // 원하는 색상으로 변경
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            NavigationHost(navController = navController)
        }
    }
}
