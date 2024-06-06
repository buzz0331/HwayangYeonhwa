package com.example.teamproject.viewmodel

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class Repository(private val table: DatabaseReference) {

    suspend fun insertUser(userData: UserData) {
        table.child(userData.UserId).setValue(userData)
    }

    suspend fun deleteItem(userData: UserData) {
        table.child(userData.UserId.toString()).removeValue()
    }

    fun initializeUserList(callback: (List<UserData>) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserData>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserData::class.java)
                    user?.let { userList.add(it) }
                }
                Log.d("Repository", "User list initialized with ${userList.size} users.")
                callback(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Repository", "Database error: ${error.message}")
            }
        }
        table.addListenerForSingleValueEvent(listener)
    }
}
