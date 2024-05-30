package com.example.teamproject.register.user


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class Repository(private val table: DatabaseReference) {

    suspend fun InsertUser(userData: UserData) {
        table.child(userData.UserId).setValue(userData)

    }
    suspend fun DeleteItem(userData: UserData) {
        table.child(userData.UserId.toString()).removeValue()
    }
    suspend fun CheckInfo(id:String,pw:String):Boolean{
        var result = false
        val listener = object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (usersnapshot in snapshot.children){
                    val user = usersnapshot.getValue(UserData::class.java)
                    user?.let {
                        if (it.UserId==(id)&&it.UserPw==(pw)){
                            result = true
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        table.addValueEventListener(listener)
        table.removeEventListener(listener)

        return result
    }


}