package com.example.teamproject.viewmodel

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

class Repository(private val table: DatabaseReference) {

//    suspend fun insertUser(userData: UserData) {
//        table.child(userData.UserId).setValue(userData)
//    }
//
//    suspend fun deleteItem(userData: UserData) {
//        table.child(userData.UserId.toString()).removeValue()
//    }
//
//    fun initializeUserList(callback: (List<UserData>) -> Unit) {
//        val listener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val userList = mutableListOf<UserData>()
//                for (userSnapshot in snapshot.children) {
//                    val user = userSnapshot.getValue(UserData::class.java)
//                    user?.let { userList.add(it) }
//                }
//                Log.d("Repository", "User list initialized with ${userList.size} users.")
//                callback(userList)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("Repository", "Database error: ${error.message}")
//            }
//        }
//        table.addListenerForSingleValueEvent(listener)
//    }

    fun addUser(userData: UserData) {
        table.child("Users").child(userData.UserId).setValue(userData)
    }

    fun updateUser(userData: UserData) {
        table.child("Users").child(userData.UserId).setValue(userData)
    }

    fun deleteUser(userId: String) {
        table.child("Users").child(userId).removeValue()
    }

    suspend fun getAllUsers(): List<UserData> {
        val snapshot = table.child("Users").get().await()
        return snapshot.children.mapNotNull { it.getValue<UserData>() }
    }

    fun addLocation(locationData: LocationData) {
        table.child("Locations").child(locationData.ID.toString()).setValue(locationData)
    }

    fun updateLocation(locationData: LocationData) {
        table.child("Locations").child(locationData.ID.toString()).setValue(locationData)
    }

    fun deleteLocation(locationId: Int) {
        table.child("Locations").child(locationId.toString()).removeValue()
    }

    suspend fun getAllLocations(): List<LocationData> {
        val snapshot = table.child("Locations").get().await()
        return snapshot.children.mapNotNull { it.getValue<LocationData>() }
    }
}
