package com.example.teamproject.register.user
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NavViewModel : ViewModel(){
    var UserList = mutableStateListOf<UserData>()
    init {
        UserList.add(UserData("gksghwls915","1234","imjhin"))
    }

    var userID:String = ""
    var userPasswd:String = ""
    var userName:String = ""

    fun checkInfo(id:String, passwd:String):Boolean{
        return UserList.any { user -> user.UserId == id && user.UserPw == passwd }
    }

    fun addUserInfo(id:String, passwd:String,name:String){
        UserList.add(UserData(id,passwd,name))
    }
}
