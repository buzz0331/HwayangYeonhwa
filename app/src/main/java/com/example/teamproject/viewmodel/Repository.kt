package com.example.teamproject.viewmodel

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

class Repository(private val table: DatabaseReference) {

    fun addUser(userData: UserData) {
        table.child("Users").child(userData.UserId).setValue(userData)
    }

    suspend fun getUser(userId: String): UserData? {
        val snapshot = table.child("Users").child(userId).get().await()
        return snapshot.getValue(UserData::class.java)
    }
    suspend fun getFriendList(userId: String): List<UserData> {
        val snapshot = table.child("Users").child(userId).child("friendList").get().await()
        val friendIds = snapshot.children.mapNotNull { it.getValue(String::class.java) }

        val friends = mutableListOf<UserData>()
        for (friendId in friendIds) {
            val friendSnapshot = table.child("Users").child(friendId).get().await()
            val friend = friendSnapshot.getValue(UserData::class.java)
            friend?.let { friends.add(it) }
        }
        return friends
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
