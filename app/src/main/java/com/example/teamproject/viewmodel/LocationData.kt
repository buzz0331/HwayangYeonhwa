package com.example.teamproject.viewmodel

data class LocationData(
    var Category: Int, // 장소 카테고리
    var Name: String, // 장소 이름
    var ID: Int,
    var isAccepted: Boolean = false,
    var review : String,
    var imageUrl:String,
    var PosReview: MutableList<Int>?, //
    var NegReview: MutableList<Int>?
){
    fun updatePosReview(index: Int) {
        PosReview?.set(index, 1)
    }

    fun updataNegReview(index: Int){
        NegReview?.set(index, 1)
    }


}
