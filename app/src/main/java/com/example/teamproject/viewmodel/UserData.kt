package com.example.teamproject.viewmodel

data class UserData(
    var UserId:String,
    var UserPw:String,
    var UserName:String,
    var isMaster:Boolean,
    var favoriteLocation: MutableList<LocationData>?,
    var friendList: MutableList<String>?,
    var sentFriendRequests: MutableList<String>?, // 보낸 친구 요청
    var receivedFriendRequests: MutableList<String>? // 받은 친구 요청
){
    constructor():this("admin","1234","admin",false, mutableListOf() ,mutableListOf(), mutableListOf(), mutableListOf())
}
