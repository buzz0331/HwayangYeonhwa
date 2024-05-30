package com.example.teamproject.register.user

import com.example.teamproject.Mainactivity.location.LocationData

data class UserData(
    var UserId:String,
    var UserPw:String,
    var UserName:String,

    var favoriteLocation: MutableList<LocationData>,
    var friendList: MutableList<UserData>
){
    constructor():this("admin","1234","admin", mutableListOf() ,mutableListOf())
}