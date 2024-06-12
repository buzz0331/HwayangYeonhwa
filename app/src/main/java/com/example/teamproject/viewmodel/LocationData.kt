package com.example.teamproject.viewmodel

data class LocationData(
    var Category: Int, // 장소 카테고리
    var Name: String, // 장소 이름
    var ID: Int,
    var isAccepted: Boolean = false,
    var review : String,
    var imageUrl:String,
    var PosReview: MutableList<MutableList<String>>? = MutableList(6){mutableListOf()}, //
    var NegReview: MutableList<MutableList<String>>? = MutableList(6){mutableListOf()}
){
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
