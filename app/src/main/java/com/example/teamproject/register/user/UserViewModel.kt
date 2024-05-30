package com.example.teamproject.register.user

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UserViewModel(private val repository: Repository) : ViewModel() {
    var UserList = mutableStateListOf<UserData>()

    init {
        repository.initializeUserList { userList ->
            UserList.clear()
            UserList.addAll(userList)
            Log.d("UserViewModel", "UserList initialized with ${UserList.size} users.")
        }
    }

    fun userInit(userData: UserData) {
        viewModelScope.launch {
            repository.insertUser(userData)
        }
    }

    fun checkInfo(id: String, passwd: String): Boolean {
        Log.d("Repository", "Checking info for user ID: $id")
        val result = UserList.any { user -> user.UserId == id && user.UserPw == passwd }
        Log.d("Repository", "Login ${if (result) "successful" else "failed"} for user ID: $id")
        return result
    }
}
