package com.example.teamproject.register.user

import com.example.teamproject.Mainactivity.location.LocationData

data class UserData(
    var UserId:String,
    var UserPw:String,
    var UserName:String,
    var isMaster:Boolean,
    var favoriteLocation: MutableList<LocationData>?,
    var friendList: MutableList<String>?
){
    constructor():this("admin","1234","admin",false, mutableListOf() ,mutableListOf())
}