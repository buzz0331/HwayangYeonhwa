package com.example.teamproject.Mainactivity.location

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamproject.NavRoutes
import com.example.teamproject.register.LocalNavGraphViewModelStoreOwner
import com.example.teamproject.register.user.Repository
import com.example.teamproject.register.user.UserViewModel
import com.example.teamproject.register.user.UserViewModelFactory
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun PlaceList(navController: NavController) {
    val table = Firebase.database.getReference("UserDB/Users")
    val navViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(Repository(table)),
        viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showButton by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = navViewModel.LocationList) {item->
                PlaceCell(location_name = item.Name){
                    navViewModel.setLocation(item.ID)
                    navController.navigate(NavRoutes.PlaceInfoScreen.route)
                }
            }
        }
        AnimatedVisibility(visible = showButton) {
            ScrollToTopButton {
                scope.launch {
                    state.scrollToItem(0)
                }
            }
        }
    }
}

@Composable
fun ScrollToTopButton(goToTop: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .size(50.dp)
                .align(Alignment.BottomEnd),
            onClick = goToTop
        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = "go to top"
            )
        }
    }
}