package com.example.teamproject.register.user
data class UserData(
    var UserId:String,
    var UserPw:String,
    var UserName:String

){
    constructor():this("admin","1234","admin")
}