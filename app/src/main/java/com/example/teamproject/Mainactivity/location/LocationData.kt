package com.example.teamproject.Mainactivity.location

data class LocationData(
    var Category: Int, // 장소 카테고리
    var Name: String, // 장소 이름
    var ID: Int,
    var isAccepted: Boolean = false,
    
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
