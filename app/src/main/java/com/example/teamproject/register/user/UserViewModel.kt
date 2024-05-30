package com.example.teamproject.register.user

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.teamproject.Mainactivity.location.LocationData
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
    var LocationList = mutableStateListOf<LocationData>()
    init {
        LocationList.add(LocationData(1,"화양연화",1,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))
        LocationList.add(LocationData(1,"맥도날드",2,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))
        LocationList.add(LocationData(2,"스타벅스",3,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))
        LocationList.add(LocationData(2,"메가커피",4,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))
    }

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

    fun updatePosReview(locationIndex: Int, reviewIndex: Int) {
        val locationData = LocationList[locationIndex]
        locationData.PosReview[reviewIndex] += 1
        LocationList[locationIndex] = locationData // 객체를 업데이트하고 다시 리스트에 할당
    }

    fun updateNegReview(locationIndex: Int, reviewIndex: Int) {
        val locationData = LocationList[locationIndex]
        locationData.NegReview[reviewIndex] += 1
        LocationList[locationIndex] = locationData // 객체를 업데이트하고 다시 리스트에 할당
    }
}
