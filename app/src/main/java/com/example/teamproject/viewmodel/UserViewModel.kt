package com.example.teamproject.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UserViewModel(private val repository: Repository) : ViewModel() {
    var UserList = mutableStateListOf<UserData>()
    var LocationList = mutableListOf<LocationData>()
    //현재 로그인 중인 user 정보
    var User = mutableStateOf<UserData>(UserData("","","", false,null, null))
    var Location = mutableStateOf<LocationData>(LocationData(0,"",0,false,"","",null,null))
    var friend_locations = mutableStateListOf<LocationData>()
    var friendList = mutableStateListOf<UserData>()
    var friend = mutableStateOf<UserData>(UserData("","","", false,null, null))

    init {
        initFunction()
    }

//    init {
//        repository.initializeUserList { userList ->
//            UserList.clear()
//            UserList.addAll(userList)
//            Log.d("UserViewModel", "UserList initialized with ${UserList.size} users.")
//        }
//    }

    fun userInit(userData: UserData) {
        viewModelScope.launch {
            repository.insertUser(userData)
        }
    }

    fun checkInfo(id: String, passwd: String): Boolean {
        Log.d("Repository", "Checking info for user ID: $id")
        val result = UserList.any { user -> user.UserId == id && user.UserPw == passwd }
        Log.d("Repository", "Login ${if (result) "successful" else "failed"} for user ID: $id")
        return result
    }

    fun checkMaster(id:String,passwd: String):Boolean{
        val result = UserList.any{ user -> user.UserId == id && user.UserPw == passwd && user.isMaster }
        return result
    }

    fun setUser(id: String){
        //해당 id 값을 user
        //로그인시 사용
        for (user in UserList){
            if(user.UserId == id)
                User = mutableStateOf(user)
        }
        setFriendList(User)
        Log.d("Repository","${User}")
    }

    fun getUser(id:String): UserData? {
        for(user in UserList){
            if(user.UserId == id)
                return user
        }
        return null
    }

    fun setLocation(id: Int){
        //장소 리스트에서 장소 선택시
        for (location in LocationList){
            if(location.ID == id){
                Location = mutableStateOf(location)
            }
        }
    }

    fun setFriendList(user: MutableState<UserData>){
        //로그인시
        friendList.clear() // 기존의 friendList를 초기화합니다.

        user.value.friendList?.forEach { friendId ->
            val friendData = getUser(friendId)
            if (friendData != null) {
                friendList.add(friendData)
            }
        }
    }

    fun setFriendLocations(friend: MutableState<UserData>) {
        // 현재 인자로 들어온 friend의 favoriteLocation을 friend_locations에 넣기
        friend_locations.clear()

        friend.value.favoriteLocation?.forEach { locationData ->
            friend_locations.add(locationData)
        }
    }

    fun setFriend(friend: UserData){
        //친구 리스트에서 친구 눌렀을때
        this.friend = mutableStateOf(friend)
    }

    fun addLocation(locationData: LocationData) {
        LocationList.add(locationData)

    }

    fun setLocation(locationData: LocationData){

    }

    fun updatePosReview(locationIndex: Int, reviewIndex: Int) {
        val locationData = LocationList[locationIndex]
        locationData.PosReview?.set(reviewIndex, 1)
        LocationList[locationIndex] = locationData // 객체를 업데이트하고 다시 리스트에 할당
    }

    fun updateNegReview(locationIndex: Int, reviewIndex: Int) {
        val locationData = LocationList[locationIndex]
        locationData.NegReview?.set(reviewIndex, 1)
        LocationList[locationIndex] = locationData // 객체를 업데이트하고 다시 리스트에 할당
    }


    fun initFunction(){
        //초기화할때 사용하는 함수
        LocationList.add(
            LocationData(
                1, "화양연화", 1, true,
                "화양연화는 짜장면, 짬뽕, 탕수육을 파는 중국집입니다.",
                "https://example.com/image1.jpg|https://example.com/menu1.jpg",
                mutableListOf(0, 0, 0, 0, 0, 0),
                mutableListOf(0, 0, 0, 0, 0, 0)
            )
        )
        LocationList.add(
            LocationData(
                1, "맥도날드", 2, true,
                "맥도날드는 전 세계적으로 유명한 패스트푸드 체인입니다.",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA4MDVfMTkx%2FMDAxNjkxMjQ2MTcxNzA3.Nm9x8Ew0StOv988Kk7ipffMK5Wu5xiBSp-pCLdvAJqog.lcYQp7KWATeF4QQgUoIuhhL05IWL80sRJk9_un6y6k0g.JPEG.rlduael22%2Foutput_1818454878.jpg&type=sc960_832|" +
                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA3MjFfMjg1%2FMDAxNjg5ODkzOTA3MTcz.DSYIthVA4gJ307uPrezU8uW4-8aNG4J4hgxsOOLoww4g.9jY7Z9K0PLlI2FVV_1iGDKCh9lRqKslkT9SbKgqtaWYg.JPEG.sb2sb2%2FIMG_9614.jpg&type=sc960_832",
                mutableListOf(0, 0, 0, 0, 0, 0),
                mutableListOf(0, 0, 0, 0, 0, 0)
            )
        )
        LocationList.add(
            LocationData(
                2, "스타벅스", 3, true,
                "스타벅스는 커피와 다양한 음료를 판매하는 카페입니다.",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjA5MjVfMzMg%2FMDAxNjY0MDMxNjI5MjM1.gv6W4pJJe1cdCLdG4NcWWJlrJ-2ddKqo5OODDCLbGFkg.UbtC7C7Tq62v-lsuC7gZgtRFXuKNIm4f79bUubYp9EUg.JPEG.china_lab%2Fa5761d81e961691f21b676a8a61f8d75.jpeg&type=sc960_832",
                mutableListOf(0, 0, 0, 0, 0, 0),
                mutableListOf(0, 0, 0, 0, 0, 0)
            )
        )
        LocationList.add(
            LocationData(
                2, "메가커피", 4, false,
                "메가커피는 다양한 커피 음료를 제공합니다.",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDA1MDNfMTc0%2FMDAxNzE0NzI2OTYxMjEx.rSDkm2roBpv7HnRYJQZPo1HA7f-hzUm0TSPZ8GBFR8Mg.yF7KRdeEEifa18ph5Xt065V5R1C4APYs-vuiXwFJ7U4g.JPEG%2F%25B8%25DE%25B0%25A1%25C3%25A2%25BE%25F7.jpg&type=sc960_832",
                mutableListOf(0, 0, 0, 0, 0, 0),
                mutableListOf(0, 0, 0, 0, 0, 0)
            )
        )
        LocationList.add(
            LocationData(
                3, "락볼링장", 5, true,
                "락볼링장은 즐거운 볼링 체험을 제공합니다.",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fimage.nmv.naver.net%2Fblog_2024_04_30_1117%2FehYRxR3ZB3_01.jpg&type=sc960_832",
                mutableListOf(0, 0, 0, 0, 0, 0),
                mutableListOf(0, 0, 0, 0, 0, 0)
            )
        )
        LocationList.add(
            LocationData(
                3, "방탈출카페", 6, true,
                "방탈출카페는 재미있는 방탈출 게임을 제공합니다.",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAxODA3MDJfMTIy%2FMDAxNTMwNDU3NDIzNDg1.xIfb3QQzv_p0F1j_z5ke1qiPtis2oeufYTNvoBAUIPkg.7NDXIGsUjq5SZaOxbVrLnDI4WOlYc_5OBTdxL4IKc7Ig.PNG.shfurgkwk70%2F%25B1%25D7%25B8%25B24.png&type=sc960_832",
                mutableListOf(0, 0, 0, 0, 0, 0),
                mutableListOf(0, 0, 0, 0, 0, 0)
            )
        )

        UserList.add(
            UserData(
                "master", "master", "master", true, mutableListOf(),
                mutableListOf()
            )
        )
        UserList.add(
            UserData(
                "user", "user", "user", false,
                mutableListOf(
                    LocationData(
                        1, "화양연화", 1, false,
                        "화양연화는 짜장면, 짬뽕, 탕수육을 파는 중국집입니다.",
                        "https://example.com/image1.jpg|https://example.com/menu1.jpg",
                        mutableListOf(0, 0, 0, 0, 0, 0),
                        mutableListOf(0, 0, 0, 0, 0, 0)
                    )
                ),
                mutableListOf("friend1", "friend2")
            )
        )
        UserList.add(
            UserData(
                "friend1", "user", "friend1", false,
                mutableListOf(
                    LocationData(
                        1, "화양연화", 1, false,
                        "화양연화는 짜장면, 짬뽕, 탕수육을 파는 중국집입니다.",
                        "https://example.com/image1.jpg|https://example.com/menu1.jpg",
                        mutableListOf(0, 0, 0, 0, 0, 0),
                        mutableListOf(0, 0, 0, 0, 0, 0)
                    )
                ),
                mutableListOf("friend2", "user")
            )
        )
        UserList.add(
            UserData(
                "friend2", "user", "friend2", false,
                mutableListOf(
                    LocationData(
                        1, "화양연화", 1, false,
                        "화양연화는 짜장면, 짬뽕, 탕수육을 파는 중국집입니다.",
                        "https://example.com/image1.jpg|https://example.com/menu1.jpg",
                        mutableListOf(0, 0, 0, 0, 0, 0),
                        mutableListOf(0, 0, 0, 0, 0, 0)
                    )
                ),
                mutableListOf("friend2", "friend1")
            )
        )

    }



}

