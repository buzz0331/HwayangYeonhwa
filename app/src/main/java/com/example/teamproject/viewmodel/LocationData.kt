package com.example.teamproject.viewmodel

import com.google.android.gms.maps.model.LatLng

data class LocationData(
    var Category: Int, // 장소 카테고리
    var Name: String, // 장소 이름
    var ID: Int,
    var isAccepted: Boolean = false,
    var review : String,
    var data : String,
    var imageUrl:String,
    var PosReview: MutableList<MutableList<String>>? = MutableList(6){mutableListOf()}, //
    var NegReview: MutableList<MutableList<String>>? = MutableList(6){mutableListOf()},
    var Location: LatLng?
){
    constructor():this(1,"화양연화",0,true,"","","", MutableList(6){mutableListOf("user")}, MutableList(6){mutableListOf("user")},null)
    fun updatePosReview(index: Int, userID: String) {
        PosReview?.get(index)?.let { reviewList ->
            if (userID in reviewList) {
                reviewList.remove(userID)
            } else {
                reviewList.add(userID)
            }
        }
    }
    fun updateNegReview(index: Int, userID: String) {
        NegReview?.get(index)?.let { reviewList ->
            if (userID in reviewList) {
                reviewList.remove(userID)
            } else {
                reviewList.add(userID)
            }
        }
    }
}