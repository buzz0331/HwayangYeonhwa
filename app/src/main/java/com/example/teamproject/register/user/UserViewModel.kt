package com.example.teamproject.register.user

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    var LocationList = mutableListOf<LocationData>()
    //현재 로그인 중인 user 정보
    var User = mutableStateOf<UserData>(UserData("","","", false,null, null))
    var Location = mutableStateOf<LocationData>(LocationData(0,"",0,false,null,null))

    var friendList = mutableStateListOf<UserData>()

    init {
        LocationList.add(LocationData(1,"화양연화",1,true,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))
        LocationList.add(LocationData(1,"맥도날드",2,true,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))
        LocationList.add(LocationData(2,"스타벅스",3,true,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))
        LocationList.add(LocationData(2,"메가커피",4,false,
            mutableListOf(0,0,0,0,0,0),
            mutableListOf(0,0,0,0,0,0)))

        UserList.add(UserData("master","master","master",true, mutableListOf(),
            mutableListOf()
        ))
        UserList.add(UserData("user","user","user",false,
            mutableListOf(LocationData(1,"화양연화",1,false,
                mutableListOf(0,0,0,0,0,0),
                mutableListOf(0,0,0,0,0,0))) ,
            mutableListOf("friend1","friend2")))
        UserList.add(UserData("friend1","user","friend1",false,
            mutableListOf(LocationData(1,"화양연화",1,false,
                mutableListOf(0,0,0,0,0,0),
                mutableListOf(0,0,0,0,0,0))) ,
            mutableListOf("friend2","user")))
        UserList.add(UserData("friend2","user","friend2",false,
            mutableListOf(LocationData(1,"화양연화",1,false,
                mutableListOf(0,0,0,0,0,0),
                mutableListOf(0,0,0,0,0,0))) ,
            mutableListOf("friend2","friend1")))
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

    fun checkMaster(id:String,passwd: String):Boolean{
        val result = UserList.any{ user -> user.UserId == id && user.UserPw == passwd && user.isMaster }
        return result
    }

    fun setUser(id: String){
        //해당 id 값을 user
        for (user in UserList){
            if(user.UserId == id)
                User = mutableStateOf(user)
        }
        setFriendList(User)
    }

    fun getUser(id:String): UserData? {
        for(user in UserList){
            if(user.UserId == id)
                return user
        }
        return null
    }

    fun setLocation(id: Int){
        for (location in LocationList){
            if(location.ID == id){
                Location = mutableStateOf(location)
            }
        }
    }

    fun setFriendList(user: MutableState<UserData>){
        friendList.clear() // 기존의 friendList를 초기화합니다.
        Log.d("Repository","${user.value.friendList}")
        user.value.friendList?.forEach { friendId ->
            val friendData = getUser(friendId)
            if (friendData != null) {
                friendList.add(friendData)
            }
        }
    }



    fun updatePosReview(locationIndex: Int, reviewIndex: Int) {
        val locationData = LocationList[locationIndex]
        locationData.PosReview?.set(reviewIndex, 1)
        LocationList[locationIndex] = locationData // 객체를 업데이트하고 다시 리스트에 할당
    }

    fun updateNegReview(locationIndex: Int, reviewIndex: Int) {
        val locationData = LocationList[locationIndex]
        locationData.NegReview?.set(reviewIndex, 1)
        LocationList[locationIndex] = locationData // 객체를 업데이트하고 다시 리스트에 할당
    }
}
