package com.example.teamproject.Mainactivity.location

data class LocationData(
    var Category: Int, // 장소 카테고리
    var Name: String, // 장소 이름
    var ID: Int,

    var PosReview: MutableList<Int>, //

    var NegReview: MutableList<Int>
){
    fun updatePosReview(index: Int) {
        PosReview[index] += 1
    }

    fun updataNegReview(index: Int){
        NegReview[index] += 1
    }


}
