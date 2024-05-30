package com.example.teamproject.register.user
import androidx.compose.runtime.mutableStateListOf
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
class UserViewModel(private val repository: Repository) : ViewModel(){
    var UserList = mutableStateListOf<UserData>()
    fun UserInit(userData: UserData){
        viewModelScope.launch {
        repository.InsertUser(userData)
        }
    }

    fun checkInfo(id:String, passwd:String):Boolean{
        return UserList.any { user -> user.UserId == id && user.UserPw == passwd }

        }
}





